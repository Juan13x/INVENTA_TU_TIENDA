package com.juanguicj.inventa_tu_tienda.main

import kotlin.collections.iterator as iterator1

data class ProductsType(val name: String, val amount: Int, val price: Float)

class PersistentClass{
    /*!Info for the last user*/
    private var user: String = ""
    /*!Info for the last Category selected*/
    private var category: String = ""

    fun setUser(newName: String){ this.user = newName }
    fun setCategory(newCategory: String){ this.category = newCategory }
    fun clearUser(){ this.user = "" }
    fun clearCategory(){ this.category = "" }
    fun getUser(): String{ return this.user }
    fun getCategory(newCategory: String): String{ return this.category }
    fun isSessionActive(): Boolean{ return this.user != "" }

    /*! Persistent dataBase. */
    private var dictionary:
            MutableMap<String, MutableMap<String, ProductsType>> = mutableMapOf("" to
            mutableMapOf("" to ProductsType("", 0, 0.0f)))

    /**
     * Check if the persistent DataBase already exists, otherwise
     * it is created
     */
    fun create():Boolean{
        this.addCategory("Aseo")
        this.addCategory("Comida")
        this.addCategory("Revuelto")
        
        this.setProduct(
            "Aseo",
            "135",
            ProductsType("Fabuloso 400ml", 12, 1400.0f))
        this.setProduct(
            "Aseo",
            "105",
            ProductsType("Limpido 400ml", 10, 1300.0f))
        this.setProduct(
            "Aseo",
            "136",
            ProductsType("Fabuloso 250ml", 20, 1000.0f))
        
        this.setProduct(
            "Comida",
            "230",
            ProductsType("Arroz libra", 25, 2000.0f))
        this.setProduct(
            "Comida",
            "240",
            ProductsType("Leche Colanta", 8, 3600.0f))
        this.setProduct(
            "Comida",
            "250",
            ProductsType("Lecherita tubito", 26, 800.0f))

        this.setProduct(
            "Revuelto",
            "135",
            ProductsType("Tomate", 2, 2200.0f))
        this.setProduct(
            "Revuelto",
            "105",
            ProductsType("Papa", 10, 1700.0f))
        this.setProduct(
            "Revuelto",
            "136",
            ProductsType("Limon", 3, 2500.0f))
        return false
    }

    /**
     * Check if the category input is included
     */
    fun containsCategory(category: String): Boolean{
        return this.dictionary.contains(category)
    }

    /**
     * Add a new category. Check if does not exist already
     */
    fun addCategory(category: String):Boolean{
        return if(containsCategory(category)){
            false
        } else{
            this.dictionary[category] = mutableMapOf("" to ProductsType("", 0, 0.0f))
            true
        }
    }

    /**
     * Delete category
     */
    fun deleteCategory(category: String):Boolean {
        val check = containsCategory(category)
        if(check){
            this.dictionary.remove(category)
        }
        return check
    }

    /**
     * Returns the Categories
     */
    fun getCategories(): MutableSet<String>{
        return this.dictionary.keys
    }

    /**
     * Check if the product with key "code" exists
     * @return 1 for true, 0 for not found product, 2 for not found category
     */
    fun containsProduct(category: String, code:String): Int{
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
    fun setProduct(category:String, setCode: String, newProduct: ProductsType): Boolean{
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
    fun getProduct(category:String, getCode: String): ProductsType? {
        val check = containsProduct(category, getCode)
        if(check == 1){
            return this.dictionary[category]?.get(getCode)
        }
        return ProductsType("", check, 0.0f)
    }

    /**
     * Remove product from category.
     * @returns 1 for true, 0 for not found product, 2 for not found category
     */
    fun deleteProduct(category:String, code: String):Int{
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
    fun getAllProducts(category: String): MutableMap<String, ProductsType>?{
        return if(containsCategory(category))
            this.dictionary[category]
        else
            null
    }
}

class DataBaseClass{
    private class DataBase{
        /*! ProductsType dataBase. */
        var products: MutableMap<String, MutableMap<String, ProductsType>> = mutableMapOf("" to
                mutableMapOf("" to ProductsType("", 0, 0.0f)))

        /*! User Data base*/
        var password = ""

    }
    private var users: MutableMap<String, DataBase> = mutableMapOf("" to DataBase())

    /**
     * Check if the persistent DataBase already exists, otherwise
     * it is created
     */
    fun create():Boolean {
        this.addUser("Juan")
        this.setPassword("Juan", "genshin")
        this.addUser("Luis")
        this.setPassword("Luis", "impact")
        this.addUser("Karen")
        this.setPassword("Karen", "Cool")
        for (user in this.users.keys) {
            this.addCategory(user, "Aseo")
            this.addCategory(user, "Comida")
            this.addCategory(user, "Revuelto")

            this.setProduct(
                user,
                "Aseo",
                "135",
                ProductsType("Fabuloso 400ml", 12, 1400.0f)
            )
            this.setProduct(
                user,
                "Aseo",
                "105",
                ProductsType("Limpido 400ml", 10, 1300.0f)
            )
            this.setProduct(
                user,
                "Aseo",
                "136",
                ProductsType("Fabuloso 250ml", 20, 1000.0f)
            )

            this.setProduct(
                user,
                "Comida",
                "230",
                ProductsType("Arroz libra", 25, 2000.0f)
            )
            this.setProduct(
                user,
                "Comida",
                "240",
                ProductsType("Leche Colanta", 8, 3600.0f)
            )
            this.setProduct(
                user,
                "Comida",
                "250",
                ProductsType("Lecherita tubito", 26, 800.0f)
            )

            this.setProduct(
                user,
                "Revuelto",
                "135",
                ProductsType("Tomate", 2, 2200.0f)
            )
            this.setProduct(
                user,
                "Revuelto",
                "105",
                ProductsType("Papa", 10, 1700.0f)
            )
            this.setProduct(
                user,
                "Revuelto",
                "136",
                ProductsType("Limon", 3, 2500.0f)
            )
        }

        return false
    }

    /**
     * Check if the user exists
     */
    fun containsUser(user: String): Boolean{
        return this.users.contains(user)
    }

    /**
     * Add New User
     */
    fun addUser(user: String): Boolean{
        val check: Boolean = containsUser(user)
        if(!check){
            this.users[user] = DataBase()
        }
        return !check
    }

    /**
     * Remove user if included.
     */
    fun deleteUser(user: String): Boolean{
        val check = containsUser(user)
        if(check) {
            this.users.remove(user)
        }
        return check
    }

    /**
     * Returns all User Keys
     */
    fun getAllUsers(): MutableSet<String> {
        return this.users.keys
    }

    /**
     * Check if the current password for a user
     * equals another
     */
    fun isTheSamePassword(user:String, password: String): Boolean{
        return this.users[user]?.password == password
    }

    /**
     * Add a new password. Check if does not exist already
     * @return 1 for true, 0 for "the new password is the same as the old one", 2 for not existing
     * user */
    fun setPassword(user: String, password: String):Int{
        return if(containsUser(user)){
            if(isTheSamePassword(user, password)){
                0
            } else{
                this.users[user]?.password = password
                1 }
        } else
            2
    }

    /**
     * Check if the category input is included.
     * @return 1 for true, 0 for not included, 2 for not existing user
     */
    fun containsCategory(user: String, category: String): Int{
        if(containsUser(user)){
            if(this.users[user]?.products?.contains(category) == true){
                return 1 }
            return  0 }
        return 2
    }

    /**
     * Add a new category. Check if does not exist already
     * @return 1 for true, 0 category already created, 2 for not existing user
     */
    fun addCategory(user: String, category: String):Int{
        return if(containsUser(user)){
            if(containsCategory(user, category) == 1){
                0
            } else{
                this.users[user]?.products?.set(category, mutableMapOf("" to ProductsType
                    ("", 0, 0.0f)))
                1 }
        } else
            2
    }

    /**
     * Changes the category, but reassigns the value of the recent one
     * @return 1 for true, 3 not changed, same name, 0 for not included category,2 for not existing
     * user
     */
    fun changeCategory(user: String, currentCategoryName: String, newCategoryName: String):Int{
        if(currentCategoryName == newCategoryName){
            return 3
        }
        val check = containsCategory(user, currentCategoryName)
        if (check == 1){
            fun addAllProducts(user:String, newCategoryName: String, products: MutableMap<String, ProductsType>?){
                for(par in products?.asIterable()!!){
                    setProduct(user, newCategoryName, par.key, par.value)
                }
            }
            val currentCategory = getAllProducts(user, currentCategoryName)
            deleteCategory(user, currentCategoryName)
            addCategory(user, newCategoryName)
            addAllProducts(user, newCategoryName, currentCategory)
        }
        return check
    }

    /**
     * Delete a category.
     * @return 1 for true, 0 not included category, 2 for not existing user
     */
    fun deleteCategory(user: String, category: String):Int{
        val check: Int = containsCategory(user, category)
        if(check == 1){
            this.users[user]?.products?.remove(category)
        }
        return check
    }

    /**
     * Returns all categories for a user
     */
    fun getCategories(user: String): MutableSet<String>?{
        return if(containsUser(user)){
            this.users[user]?.products?.keys
        } else null
    }

    /**
     * Check if the product with key "code" exists
     * @return 1 for true, 0 for not included product, 2 for not included category
     * 3 for not existing user
     */
    fun containsProduct(user: String, category: String, code:String): Int{
        return if(containsUser(user)){
            if(containsCategory(user, category) == 1){
                if(this.users[user]?.products?.get(category)?.containsKey(code) == true)
                    1
                else
                    0
            } else{
                2 }
        } else{
            3 }
    }

    /**
     * Add a new product to a specific category
     * @return 1 for true, 0 not included category, 2 for not existing user
     */
    fun setProduct(user:String, category:String, setCode: String, newProduct: ProductsType):
            Int{
        val check: Int = containsCategory(user, category)
        if(check == 1){
            this.users[user]?.products?.get(category)?.set(setCode, newProduct)
        }
         return check
    }

    /**
     * return a product from a category
     * @return return a product from a category. If not found,
     * returns a Product with null name and amount as:
     * 0 for not included product, 2 for not included category
     * 3 for not existing user
     */
    fun getProduct(user: String, category:String, getCode: String): ProductsType? {
        val check: Int = containsProduct(user, category, getCode)
        return if(check == 1){
            this.users[user]?.products?.get(category)?.get(getCode)
        } else{
            ProductsType("", check, 0.0f)
        }
    }

    /**
     * Remove product from category.
     * @return 1 for true, 0 for not included product, 2 for not included category
     * 3 for not existing user
     */
    fun deleteProduct(user: String, category:String, code: String):Int{
        val check: Int = containsProduct(user, category, code)
        if(check == 1){
            this.users[user]?.products?.get(category)?.remove(code)
        }
        return check
    }

    /**
     * Returns all products for the selected category for the selected user.
     * @return all product for the selected category for the selected user.
     * if Product has null name, then the amount attribute means as follows:
     * 1 for true, 0 for not included product, 2 for not included category
     * 3 for not existing user
     */
    fun getAllProducts(user: String, category: String): MutableMap<String, ProductsType>?{
        val check: Int = containsCategory(user, category)
        if(check == 1){
            return this.users[user]?.products?.get(category)
        }
        return mutableMapOf("null" to ProductsType("", check, 0.0f))
    }
}

val myDictionary = PersistentClass()
val myDataBase = DataBaseClass()

/*
TEST

fun main1(){
   val myDictionary = PersistentClass()
    myDictionary.create()

    var check1 = myDictionary.addCategory("test1")
    println("creation test1 $check1")
    check1 = myDictionary.addCategory("test1")
    println("creation test1 $check1")

    var check2 = myDictionary.setProduct("test1","code1", ProductsType("name1", 5, 1000.0f))
    println("\ncreation code1 test1 $check2")
    var product = myDictionary.getProduct("test1", "code1")
    check2 = myDictionary.setProduct("test1","code1", ProductsType("name1", 5, 2000.0f))
    println("creation code1 test1 $check2")
    check2 = myDictionary.setProduct("test2","code2", ProductsType("name1", 5, 1000.0f))
    println("creation code2 test2 $check2")
    check2 = myDictionary.setProduct("test1","code3", ProductsType("name1", 15, 3000.0f))
    println("creation code3 test1 $check2")

    println("get code1 test1 $product")
    product = myDictionary.getProduct("test1", "code1")
    println("get code1 test1 $product")
    product = myDictionary.getProduct("test1", "code3")
    println("get code3 test1 $product")
    product = myDictionary.getProduct("test2", "code1")
    println("get code1 test2 $product")
    var check3 = myDictionary.containsProduct("test2", "code1")
    println("contain code1 test2 $check3")
    product = myDictionary.getProduct("test1", "code2")
    println("get code2 test1 $product")

    check1 = myDictionary.addCategory("test2")
    println("creation test2 $check1")
    check3 = myDictionary.containsProduct("test2", "code1")
    println("contain code1 test2 $check3")
    product = myDictionary.getProduct("test2", "code1")
    println("get code1 test2 $product")
    check2 = myDictionary.setProduct("test2","code2", ProductsType("name2", 20, 1050.0f))
    println("creation code2 test2 $check2")
    product = myDictionary.getProduct("test2", "code1")
    println("get code1 test2 $product")
    check2 = myDictionary.setProduct("test2","code1", ProductsType("name2", 19, 1050.0f))
    println("creation code1 test2 $check2")
    check3 = myDictionary.containsProduct("test2", "code1")
    println("contain code1 test2 $check3")
    product = myDictionary.getProduct("test2", "code1")
    println("get code1 test2 $product")

    check3 = myDictionary.deleteProduct("test1", "code4")
    println("\ndelete code4 test1 $check3")
    check3 = myDictionary.deleteProduct("test1", "code1")
    println("delete code1 test1 $check3")
    product = myDictionary.getProduct("test1", "code1")
    println("get code1 test1 $product")

    product = myDictionary.getProduct("Aseo", "105")
    println("\nget Aseo 105 $product")
    check1 = myDictionary.deleteCategory("Aseo")
    println("delete Aseo $check1")
    check3 = myDictionary.containsProduct("Aseo", "105")
    println("contain 105 Aseo $check3")
    product = myDictionary.getProduct("Aseo", "105")
    println("get Aseo 105 $product")

    product = myDictionary.getProduct("Comida", "250")
    println("\nget Comida 250 $product")
    check3 = myDictionary.deleteProduct("Comida", "250")
    println("delete 250 Aseo $check3")
    check3 = myDictionary.containsProduct("Comida", "250")
    println("contain 250 Comida $check3")

    var allP = myDictionary.getAllProducts("Comida")
    println("\nAll products Comida $allP")
    var allC = myDictionary.getCategories()
    println("All categories $allC")
}

fun main2(){
    val myDataBase = DataBaseClass()
    myDataBase.create()

    var check1 = myDataBase.addCategory("Juan","test1")
    println("creation test1 Juan $check1")
    check1 = myDataBase.addCategory("Juan","test1")
    println("creation test1 Juan $check1")

    var check2 = myDataBase.setProduct("Juan","test1","code1", ProductsType("name1", 5, 1000.0f))
    println("\ncreation code1 Juan $check2")
    var product = myDataBase.getProduct("Juan","test1", "code1")
    check2 = myDataBase.setProduct("Juan","test1","code1", ProductsType("name1", 5, 2000.0f))
    println("creation code1 Juan $check2")
    check2 = myDataBase.setProduct("Juan","test2","code2", ProductsType("name1", 5, 1000.0f))
    println("creation code2 test2 Juan $check2")
    check2 = myDataBase.setProduct("Juan","test1","code3", ProductsType("name1", 15, 3000.0f))
    println("creation code3 Juan $check2")

    println("get code1 Juan $product")
    product = myDataBase.getProduct("Juan","test1", "code1")
    println("get code1 Juan $product")
    product = myDataBase.getProduct("Juan","test1", "code3")
    println("get code3 Juan $product")
    product = myDataBase.getProduct("Juan","test2", "code1")
    println("get code1 test2 Juan $product")
    var check3 = myDataBase.containsProduct("Juan","test2", "code1")
    println("contain code1 test2 Juan $check3")
    product = myDataBase.getProduct("Juan","test1", "code2")
    println("get code2 test1 Juan $product")

    check1 = myDataBase.addCategory("Juan","test2")
    println("creation test2 Juan $check1")
    check3 = myDataBase.containsProduct("Juan","test2", "code1")
    println("contain code1 test2 Juan $check3")
    product = myDataBase.getProduct("Juan","test2", "code1")
    println("get code1 test2 Juan $product")
    check2 = myDataBase.setProduct("Juan","test2","code2", ProductsType("name2", 20, 1050.0f))
    println("creation code2 test2 Juan $check2")
    product = myDataBase.getProduct("Juan","test2", "code1")
    println("get code1 test2 Juan $product")
    check2 = myDataBase.setProduct("Juan","test2","code1", ProductsType("name2", 19, 1050.0f))
    println("creation code1 test2 Juan $check2")
    check3 = myDataBase.containsProduct("Juan","test2", "code1")
    println("contain code1 test2 Juan $check3")
    product = myDataBase.getProduct("Juan","test2", "code1")
    println("get code1 test2 Juan $product")

    check1 = myDataBase.deleteProduct("Juan","test1", "code4")
    println("\ndelete code4 test1 Juan $check1")
    check1 = myDataBase.deleteProduct("Juan","test1", "code1")
    println("delete code1 test1 Juan $check1")
    product = myDataBase.getProduct("Juan","test1", "code1")
    println("get code1 Juan $product")

    product = myDataBase.getProduct("Juan","Aseo", "105")
    println("\nget Aseo 105 Juan $product")
    check1 = myDataBase.deleteCategory("Juan","Aseo")
    println("delete Aseo Juan $check1")
    check3 = myDataBase.containsProduct("Juan","Aseo", "105")
    println("contain 105 Aseo Juan $check3")
    product = myDataBase.getProduct("Juan","Aseo", "105")
    println("get Aseo 105 Juan $product")

    product = myDataBase.getProduct("Juan","Comida", "250")
    println("\nget Comida 250 Juan $product")
    check1 = myDataBase.deleteProduct("Juan","Comida", "250")
    println("delete 250 Comida Juan $check1")
    check3 = myDataBase.containsProduct("Juan","Comida", "250")
    println("contain 250 Comida Juan $check3")

    var check4 = myDataBase.addUser("Mateo")
    println("\ncreation Mateo $check4")
    check2 = myDataBase.setProduct("Mateo","test2","code2", ProductsType("name2", 20, 1050.0f))
    println("creation code2 test2 Mateo $check2")
    check1 = myDataBase.addCategory("Mateo","test2")
    println("creation test2 Mateo $check1")
    product = myDataBase.getProduct("Mateo","test2", "code1")
    println("get code1 test2 Mateo $product")
    check2 = myDataBase.setProduct("Mateo","test2","code1", ProductsType("name2", 19, 1050.0f))
    println("creation code1 test2 Mateo $check2")
    check3 = myDataBase.containsProduct("Mateo","test2", "code1")
    println("contain code1 test2 Mateo $check3")
    product = myDataBase.getProduct("Mateo","test2", "code1")
    println("get code1 test2 Mateo $product")

    check4 = myDataBase.deleteUser("Juan")
    println("\nDelete Juan $check4")
    product = myDataBase.getProduct("Juan","Comida", "250")
    println("get Comida 250 Juan $product")
    check1 = myDataBase.deleteProduct("Juan","Comida", "250")
    println("delete 250 Comida Juan $check1")
    check3 = myDataBase.containsProduct("Juan","Comida", "250")
    println("contain 250 Comida Juan $check3")
}

fun main(){
    main1()
    println("\n\n#############################")
    main2()
}
*/