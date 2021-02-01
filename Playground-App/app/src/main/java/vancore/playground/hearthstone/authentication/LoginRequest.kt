package vancore.playground.hearthstone.authentication

import com.google.gson.annotations.SerializedName

data class LoginRequest(

    //grant_type:client_credentials
    //curl -u {client_id}:{client_secret} -d grant_type=client_credentials https://us.battle.net/oauth/token
    @SerializedName("client_request")
    var client_request: String,
    
    @SerializedName("client_secret")
    var client_secret: String
)