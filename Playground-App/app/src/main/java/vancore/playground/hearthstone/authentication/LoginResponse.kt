package vancore.playground.hearthstone.authentication

import com.google.gson.annotations.SerializedName

class LoginResponse (

    @SerializedName("status_code")
    var statusCode: Int,

    @SerializedName("access_token")
    var accessToken: String,

    @SerializedName("token_Type")
    var tokenType: String,

    @SerializedName("expires_in")
    var expiresIn: String,

    @SerializedName("scope")
    var scope: String

// Blizzard Example Response:
// {"access_token": "USVb1nGO9kwQlhNRRnI4iWVy2UV5j7M6h7",
// "token_type": "bearer",
// "expires_in": 86399,
// "scope": "example.scope"}

)