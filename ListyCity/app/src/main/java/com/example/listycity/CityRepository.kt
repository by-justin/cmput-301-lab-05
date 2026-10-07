package com.example.listycity

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.firestore.FirebaseFirestore


class CityRepository {

    private val db = FirebaseFirestore.getInstance()
    private val citiesCollection = db.collection("cities")
    private val _cities = mutableStateListOf<City>()

    init {
        listenForCities()
    }

    val cities: List<City>
        get() = _cities

    fun addCity(city: City) {
        var document = citiesCollection.document()
        var cityWId = city.copy(
            id = document.id
        )
        // _cities.add(city)
        document.set(cityWId)
    }

    fun delCity(city: City) {
//        _cities.remove(city)
        citiesCollection.document(city.id).delete()
    }

    fun updateCity(oldCity: City, updatedCity: City) {
        var cityWId = updatedCity.copy(
            id = oldCity.id
        )
        // _cities[_cities.indexOf(oldCity)] = updatedCity
        citiesCollection.document(oldCity.id).set(cityWId)
    }

    private fun listenForCities() {
        citiesCollection.addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            if (snapshot != null) {
                val cities = snapshot.toObjects(City::class.java)
                _cities.clear()
                _cities.addAll(cities)
            }
        }
    }
}