package api.requests.skelethon.interfaces;

import api.models.BaseModel;

public interface CrudEndpointInterface {
    Object post (BaseModel baseModel);
    Object get ();
    Object put (BaseModel baseModel);
    Object delete (int id);
}
