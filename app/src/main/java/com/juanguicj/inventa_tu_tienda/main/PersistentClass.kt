package com.juanguicj.inventa_tu_tienda.main

import com.juanguicj.inventa_tu_tienda.persistance.LocalRepository
import com.juanguicj.inventa_tu_tienda.persistance.category.Category
import com.juanguicj.inventa_tu_tienda.persistance.user.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PersistentClass{
    private val systemName = "systemName"
    private val myUserDAO = LocalRepository.user_db.userDAO()
    private val myCategoryDAO = LocalRepository.category_db.categoryDAO()

    private suspend fun isUserTableCreated(): Boolean{
        return this.myUserDAO.checkTableCreated(this.systemName) != 0
    }
    suspend fun setUser(newName: String){
        val user = User(this.systemName, newName, "")
        this.myUserDAO.setUser(user)
    }
    suspend fun setCategory(newCategory: String){
        val user = User(this.systemName, this.getUser(), newCategory)
        this.myUserDAO.setUser(user)
    }
    suspend fun clearUser(){
        val user = User(this.systemName, "", "")
        this.myUserDAO.setUser(user)
    }
    suspend fun clearCategory(){
        val user = User(this.systemName, this.getUser(), "")
        this.myUserDAO.setUser(user)
    }
    suspend fun getUser() : String = this.myUserDAO.getUser(this.systemName).email
    suspend fun getCategory(): String = this.myUserDAO.getUser(this.systemName).category
    suspend fun isSessionActive(): Boolean{
        return this.myUserDAO.getUser(this.systemName).email != ""
    }

    /*! Persistent dataBase. */
    private var dictionary:
            MutableMap<String, MutableMap<String, ProductsType>> = mutableMapOf("" to
            mutableMapOf("" to ProductsType("", 0, 0.0f)))

    /**
     * Check if the persistent DataBase already exists, otherwise
     * it is created
     */
    fun create():Boolean{
        runBlocking{
            launch {
                if(!this@PersistentClass.isUserTableCreated()){
                    this@PersistentClass.clearUser()
                    this@PersistentClass.addCategory("Aseo")
                    this@PersistentClass.addCategory("Comida")
                    this@PersistentClass.addCategory("Revuelto")
                }
            }
        }

//        this.addCategory("Aseo")
//        this.addCategory("Comida")
//        this.addCategory("Revuelto")
//
//        this.setProduct(
//            "Aseo",
//            "101",
//            ProductsType("Fabuloso 400ml", 12, 1400.0f))
//        this.setProduct(
//            "Aseo",
//            "102",
//            ProductsType("Limpido 400ml", 10, 1300.0f))
//        this.setProduct(
//            "Aseo",
//            "103",
//            ProductsType("Fabuloso 250ml", 20, 1000.0f))
//
//        this.setProduct(
//            "Comida",
//            "201",
//            ProductsType("Arroz libra", 25, 2000.0f))
//        this.setProduct(
//            "Comida",
//            "202",
//            ProductsType("Leche Colanta", 8, 3600.0f))
//        this.setProduct(
//            "Comida",
//            "203",
//            ProductsType("Lecherita tubito", 26, 800.0f))
//
//        this.setProduct(
//            "Revuelto",
//            "301",
//            ProductsType("Tomate", 2, 2200.0f))
//        this.setProduct(
//            "Revuelto",
//            "302",
//            ProductsType("Papa", 10, 1700.0f))
//        this.setProduct(
//            "Revuelto",
//            "303",
//            ProductsType("Limon", 3, 2500.0f))
        return false
    }

    /**
     * Check if the category input is included
     */
    suspend fun containsCategory(category: String): Boolean{
        return myCategoryDAO.isCategoryIncluded(category) > 0
    }

    /**
     * Add a new category. Check if does not exist already
     */
    suspend fun addCategory(category: String):Boolean{
        return if(containsCategory(category)){
            false
        } else{
            val newCategory = Category(null, category)
            myCategoryDAO.setCategory(newCategory)
            true
        }
    }

    /**
     * Delete category
     */
    suspend fun deleteCategory(category: String):Boolean {
        val check = containsCategory(category)
        if(check){
            myCategoryDAO.deleteCategory(category)
        }
        return check
    }

    /**
     * Rename category
     * @return 1 if the category was renamed. 2 if the original and new name are the same.
     * 0 if the new name is already assigned
     */
    suspend fun renameCategory(currentCategoryName: String, newCategoryName: String):Int {
        return if(currentCategoryName == newCategoryName){
            2
        } else if(containsCategory(newCategoryName)){
            0
        } else{
            addCategory(newCategoryName)
            deleteCategory(currentCategoryName)
            1 }
    }

    /**
     * Returns the Categories
     */
    suspend fun getCategories(): MutableSet<String>{
        val data = myCategoryDAO.getCategories()
        val keys = ArrayList<String>()
        for(cat in data){
            keys.add(cat.category)
        }
        return keys.toMutableSet()
    }

    /**
     * Check if the product with key "code" exists
     * @return 1 for true, 0 for not found product, 2 for not found category
     */
    suspend fun containsProduct(category: String, code:String): Int{
        return if(containsCategory(category)){
            if(this.dictionary[category]?.containsKey(code) == true) 1 else 0
        }
        else{
            2
        }
    }

    /**
     * Add a new product to a specific category
     */
    suspend fun setProduct(category:String, setCode: String, newProduct: ProductsType): Boolean{
        val check = containsCategory(category)
        if(check){
            this.dictionary[category]?.set(setCode, newProduct)
        }
        return check
    }

    /**
     * return a product from a category
     * @return a ProductsType(val name: String?, val amount: Int, val price: Float) from a category.
     * If not found, returns a null as name attribute and the
     * amount attribute means: 0 for not found product, 2 for not found category
     */
    suspend fun getProduct(category:String, getCode: String): ProductsType? {
        val check = containsProduct(category, getCode)
        if(check == 1){
            return this.dictionary[category]?.get(getCode)
        }
        return null
    }

    /**
     * Remove product from category.
     * @returns 1 for true, 0 for not found product, 2 for not found category
     */
    suspend fun deleteProduct(category:String, code: String):Int{
        val check = containsProduct(category, code)
        if(check == 1){
            this.dictionary[category]?.remove(code)
        }
        return check
    }

    /**
     * Returns all products for a category
     * @return the products have the format:
     * ProductsType(val name: String?, val amount: Int, val price: Float).
     * if null is because the category does not exist
     */
    suspend fun getAllProducts(category: String): MutableMap<String, ProductsType>?{
        return if(containsCategory(category)) {
            val products = this.dictionary[category]
            products?.remove("")
            products
        }
        else
            null
    }
}
