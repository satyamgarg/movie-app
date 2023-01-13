package com.example.movie.data

import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(
    val adult: Boolean? = null,
    @SerializedName("backdrop_path")
    val backdropPath: String? = null,
    @SerializedName("belongs_to_collection")
    val belongsToCollection: BelongsToCollection? = null,
    val budget: Int? = null,
    val genres: List<Genre?>? = null,
    val homepage: String? = null,
    val id: Int? = null,
    val imdb_id: String? = null,
    @SerializedName("original_language")
    val originalLanguage: String? = null,
    @SerializedName("original_title")
    val originalTitle: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    @SerializedName("poster_path")
    val posterPath: String? = null,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany?>? = null,
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountry?>? = null,
    @SerializedName("release_date")
    val releaseDate: String? = null,
    val revenue: Int? = null,
    val runtime: Int? = null,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage?>? = null,
    val status: String? = null,
    val tagline: String? = null,
    val title: String? = null,
    val video: Boolean? = null,
    @SerializedName("vote_average")
    val voteAverage: Double? = null,
    @SerializedName("vote_count")
    val voteCount: Int?= null
) {
    data class BelongsToCollection(
        @SerializedName("backdrop_path")
        val backdropPath: String?,
        val id: Int?,
        val name: String?,
        @SerializedName("poster_path")
        val posterPath: String?
    )

    data class Genre(
        val id: Int?,
        val name: String?
    )

    data class ProductionCompany(
        val id: Int?,
        @SerializedName("poster_path")
        val logoPath: String?,
        val name: String?,
        @SerializedName("origin_country")
        val originCountry: String?
    )

    data class ProductionCountry(
        val iso_3166_1: String?,
        val name: String?
    )

    data class SpokenLanguage(
        @SerializedName("english_name")
        val englishName: String?,
        val iso_639_1: String?,
        val name: String?
    )
}