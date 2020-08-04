package com.hjrpc.session;

import com.hjrpc.util.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class RedisSessionComponet extends AbstractSessionDAO {
    @Autowired
    JedisUtil jedisUtil;

    private final String SHIRO_SESSION_PREFIX="shiro-session:";
    private final int DEFAULT_EXPIRE_TIME = 600;

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        saveSession(session);
        return sessionId;
    }

    private byte[] getKey(String sessionId) {
        return (SHIRO_SESSION_PREFIX+sessionId).getBytes();
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if(sessionId==null){
            return null;
        }
        byte[] key = getKey(sessionId.toString());
        byte[] value = jedisUtil.get(key);
        Session session = (Session) SerializationUtils.deserialize(value);
        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);

    }

    private void saveSession(Session session) {
        Serializable sessionId = session.getId();
        byte[] key = getKey(sessionId.toString());
        byte[] value = SerializationUtils.serialize(session);
        jedisUtil.set(key,value);
        jedisUtil.expire(key,DEFAULT_EXPIRE_TIME);
    }

    @Override
    public void delete(Session session) {
        byte[] key = getKey(session.getId().toString());
        jedisUtil.delete(key);
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<byte[]> keys = jedisUtil.keys(SHIRO_SESSION_PREFIX);
        Set<Session> sessions = new HashSet<>();
        for (byte[] key : keys) {
            byte[] bytes = jedisUtil.get(key);
            Session session = (Session) SerializationUtils.deserialize(bytes);
            sessions.add(session);
        }
        return sessions;
    }
}
