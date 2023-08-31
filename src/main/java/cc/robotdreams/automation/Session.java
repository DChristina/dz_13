package cc.robotdreams.automation;

import cc.robotdreams.automation.utils.MySQLDriver;

public class Session {
    static final private ThreadLocal<Session> _instance = new ThreadLocal<>();

    private Session (){}
    static public Session get(){
        if(_instance.get()==null){
            _instance.set(new Session());
        }
        return _instance.get();
    }

    private MySQLDriver _mysql;

    public MySQLDriver mysql(){
        if(this._mysql == null){
            this._mysql = new MySQLDriver();
        }
        return this._mysql;
    }

}
