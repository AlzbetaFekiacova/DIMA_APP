package eu.mobcomputing.dima.registration.models

import com.google.gson.annotations.SerializedName

data class AnalyzedInstruction(
    @SerializedName("name") val name: String,
    @SerializedName("steps") val steps: List<Instruction>,
)