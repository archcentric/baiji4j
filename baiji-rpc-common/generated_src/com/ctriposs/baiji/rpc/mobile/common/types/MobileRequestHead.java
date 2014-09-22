package com.ctriposs.baiji.rpc.mobile.common.types;

import java.util.*;
import com.ctriposs.baiji.exception.*;
import com.ctriposs.baiji.rpc.common.*;
import com.ctriposs.baiji.schema.*;
import com.ctriposs.baiji.specific.*;
import com.google.common.base.Objects;

@SuppressWarnings("all")
public class MobileRequestHead extends SpecificRecordBase implements SpecificRecord {
    private static final long serialVersionUID = 1L;

    public static final Schema SCHEMA = Schema.parse("{\"type\":\"record\",\"name\":\"MobileRequestHead\",\"namespace\":\"com.ctriposs.baiji.rpc.mobile.common.types\",\"doc\":null,\"fields\":[{\"name\":\"syscode\",\"type\":[\"string\",\"null\"]},{\"name\":\"lang\",\"type\":[\"string\",\"null\"]},{\"name\":\"auth\",\"type\":[\"string\",\"null\"]},{\"name\":\"cid\",\"type\":[\"string\",\"null\"]},{\"name\":\"ctok\",\"type\":[\"string\",\"null\"]},{\"name\":\"cver\",\"type\":[\"string\",\"null\"]},{\"name\":\"sid\",\"type\":[\"string\",\"null\"]},{\"name\":\"extension\",\"type\":[{\"type\":\"record\",\"name\":\"ExtensionFieldType\",\"namespace\":\"com.ctriposs.baiji.rpc.mobile.common.types\",\"doc\":null,\"fields\":[{\"name\":\"name\",\"type\":[\"string\",\"null\"]},{\"name\":\"value\",\"type\":[\"string\",\"null\"]}]},\"null\"]}]}");

    @Override
    public Schema getSchema() { return SCHEMA; }

    public MobileRequestHead(
        String syscode,
        String lang,
        String auth,
        String cid,
        String ctok,
        String cver,
        String sid,
        ExtensionFieldType extension
    ) {
        this.syscode = syscode;
        this.lang = lang;
        this.auth = auth;
        this.cid = cid;
        this.ctok = ctok;
        this.cver = cver;
        this.sid = sid;
        this.extension = extension;
    }

    public MobileRequestHead() {
    }

    /**
     * System platform code
     */
    public String syscode;


    /**
     * Language
     */
    public String lang;


    /**
     * Authentication information
     */
    public String auth;


    /**
     * Device ID
     */
    public String cid;


    /**
     * Device token (alternative)
     */
    public String ctok;


    /**
     * Version
     */
    public String cver;


    /**
     * Channel ID built in the channel package
     */
    public String sid;


    /**
     * Extension data
     */
    public ExtensionFieldType extension;

    /**
     * System platform code
     */
    public String getSyscode() {
        return syscode;
    }

    /**
     * System platform code
     */
    public void setSyscode(final String syscode) {
        this.syscode = syscode;
    }


    /**
     * Language
     */
    public String getLang() {
        return lang;
    }

    /**
     * Language
     */
    public void setLang(final String lang) {
        this.lang = lang;
    }


    /**
     * Authentication information
     */
    public String getAuth() {
        return auth;
    }

    /**
     * Authentication information
     */
    public void setAuth(final String auth) {
        this.auth = auth;
    }


    /**
     * Device ID
     */
    public String getCid() {
        return cid;
    }

    /**
     * Device ID
     */
    public void setCid(final String cid) {
        this.cid = cid;
    }


    /**
     * Device token (alternative)
     */
    public String getCtok() {
        return ctok;
    }

    /**
     * Device token (alternative)
     */
    public void setCtok(final String ctok) {
        this.ctok = ctok;
    }


    /**
     * Version
     */
    public String getCver() {
        return cver;
    }

    /**
     * Version
     */
    public void setCver(final String cver) {
        this.cver = cver;
    }


    /**
     * Channel ID built in the channel package
     */
    public String getSid() {
        return sid;
    }

    /**
     * Channel ID built in the channel package
     */
    public void setSid(final String sid) {
        this.sid = sid;
    }


    /**
     * Extension data
     */
    public ExtensionFieldType getExtension() {
        return extension;
    }

    /**
     * Extension data
     */
    public void setExtension(final ExtensionFieldType extension) {
        this.extension = extension;
    }

    // Used by DatumWriter. Applications should not call.
    public java.lang.Object get(int fieldPos) {
        switch (fieldPos) {
            case 0: return this.syscode;
            case 1: return this.lang;
            case 2: return this.auth;
            case 3: return this.cid;
            case 4: return this.ctok;
            case 5: return this.cver;
            case 6: return this.sid;
            case 7: return this.extension;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in get()");
        }
    }

    // Used by DatumReader. Applications should not call.
    @SuppressWarnings(value="unchecked")
    public void put(int fieldPos, java.lang.Object fieldValue) {
        switch (fieldPos) {
            case 0: this.syscode = (String)fieldValue; break;
            case 1: this.lang = (String)fieldValue; break;
            case 2: this.auth = (String)fieldValue; break;
            case 3: this.cid = (String)fieldValue; break;
            case 4: this.ctok = (String)fieldValue; break;
            case 5: this.cver = (String)fieldValue; break;
            case 6: this.sid = (String)fieldValue; break;
            case 7: this.extension = (ExtensionFieldType)fieldValue; break;
            default: throw new BaijiRuntimeException("Bad index " + fieldPos + " in put()");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final MobileRequestHead other = (MobileRequestHead)obj;
        return 
            Objects.equal(this.syscode, other.syscode) &&
            Objects.equal(this.lang, other.lang) &&
            Objects.equal(this.auth, other.auth) &&
            Objects.equal(this.cid, other.cid) &&
            Objects.equal(this.ctok, other.ctok) &&
            Objects.equal(this.cver, other.cver) &&
            Objects.equal(this.sid, other.sid) &&
            Objects.equal(this.extension, other.extension);
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (this.syscode == null ? 0 : this.syscode.hashCode());
        result = 31 * result + (this.lang == null ? 0 : this.lang.hashCode());
        result = 31 * result + (this.auth == null ? 0 : this.auth.hashCode());
        result = 31 * result + (this.cid == null ? 0 : this.cid.hashCode());
        result = 31 * result + (this.ctok == null ? 0 : this.ctok.hashCode());
        result = 31 * result + (this.cver == null ? 0 : this.cver.hashCode());
        result = 31 * result + (this.sid == null ? 0 : this.sid.hashCode());
        result = 31 * result + (this.extension == null ? 0 : this.extension.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("syscode", syscode)
            .add("lang", lang)
            .add("auth", auth)
            .add("cid", cid)
            .add("ctok", ctok)
            .add("cver", cver)
            .add("sid", sid)
            .add("extension", extension)
            .toString();
    }
}
