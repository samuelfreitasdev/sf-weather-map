package com.sf.sfweathermap.domain.about

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(value = "DTO with API status")
data class About(
    @ApiModelProperty(value = "Show if the API is healthy.") val healthy: Boolean,
    @ApiModelProperty(value = "Show a message about the API status.") val status: String,
)