package com.paraskcd.unitedwalls.model

data class Uploader(
    val __v: Int,
    val _id: String,
    val userID: Long,
    val username: String,
    val description: String?,
    val avatar_file_url: String?,
    val avatar_uuid: String?,
    val avatar_mime_type: String?,
    val socialMediaLinks: SocialMediaLinks?,
    val donationLinks: DonationLinks?,
    var walls: List<WallsItem>
)

data class SocialMediaLinks(
    val twitter: String?,
    val instagram: String?,
    val mastodon: String?,
    val facebook: String?,
    val threads: String?,
    val steam: String?,
    val linkedIn: String?,
    val link: String?,
    val other: List<OtherLinks>?
)

data class DonationLinks(
    val paypal: String?,
    val patreon: String?,
    val otherdonations: List<OtherLinks>?
)

data class OtherLinks(
    val title: String?,
    val link : String?
)