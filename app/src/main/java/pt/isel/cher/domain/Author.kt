package pt.isel.cher.domain

data class Author(
    val number: String,
    val firstName: String,
    val lastName: String,
    val email: String,
) {
    val fullName: String
        get() = "$firstName $lastName"
}
