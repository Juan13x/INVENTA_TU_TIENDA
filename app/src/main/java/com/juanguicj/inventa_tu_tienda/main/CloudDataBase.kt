package com.juanguicj.inventa_tu_tienda.main

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val CLOUD_Categories = false
private const val CLOUD_Products = false

class CloudDataBase{
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
        val db = Firebase.firestore

        this.addUser("juan1@app.com")
        this.setPassword("juan1@app.com", "111111")

        this.addUser("luis2@app.com")
        this.setPassword("luis2@app.com", "222222")

        this.addUser("karen3@app.com")
        this.setPassword("karen3@app.com", "3333333")

        var list = this.users.keys.toList()
        val lastIndex = list.lastIndex
        list = list.subList(1,lastIndex+1)
        var counter = 0
        for (user in list) {
            val counterSTR = counter.toString()

            this.addCategory(user, "Aseo1")
            this.addCategory(user, "Comida2")
            this.addCategory(user, "Revuelto3")

            if(CLOUD_Categories) {
                val arrayCategory = hashMapOf("0" to arrayListOf("Aseo1", "Comida2", "Revuelto3"))
                db.collection("categories")
                    .document(user)
                    .set(arrayCategory)
            }
            this.setProduct(
                user,
                "Aseo1",
                "11$counterSTR",
                ProductsType("Fabuloso 400ml", 12, 1400.0f)
            )

            this.setProduct(
                user,
                "Aseo1",
                "12$counterSTR",
                ProductsType("Limpido 400ml", 10, 1300.0f)
            )
            this.setProduct(
                user,
                "Aseo1",
                "13$counterSTR",
                ProductsType("Fabuloso 250ml", 20, 1000.0f)
            )

            this.setProduct(
                user,
                "Comida2",
                "21$counterSTR",
                ProductsType("Arroz libra", 25, 2000.0f)
            )
            this.setProduct(
                user,
                "Comida2",
                "22$counterSTR",
                ProductsType("Leche Colanta", 8, 3600.0f)
            )
            this.setProduct(
                user,
                "Comida2",
                "23$counterSTR",
                ProductsType("Lecherita tubito", 26, 800.0f)
            )

            this.setProduct(
                user,
                "Revuelto3",
                "31$counterSTR",
                ProductsType("Tomate", 2, 2200.0f)
            )
            this.setProduct(
                user,
                "Revuelto3",
                "32$counterSTR",
                ProductsType("Papa", 10, 1700.0f)
            )
            this.setProduct(
                user,
                "Revuelto3",
                "33$counterSTR",
                ProductsType("Limon", 3, 2500.0f)
            )

            counter += 1
        }

        if(CLOUD_Products){
            counter = 0
            db.runTransaction{ transaction->
                for(user in list){
                    val counterSTR = counter.toString()

                    val user = db.collection("products").document(user)
                    val catAseo = user.collection("Aseo1")
                    val catComida = user.collection("Comida2")
                    val catRevuelto = user.collection("Revuelto3")

                    var arrayProduct = hashMapOf("name" to "Fabuloso 250ml", "amount" to 20, "price" to 1000.0f)
                    var code = catAseo.document("13$counterSTR")
                    transaction.set(code, arrayProduct)

                    arrayProduct = hashMapOf("name" to "Limpido 400ml", "amount" to 10, "price" to 1300.0f)
                    code = catAseo.document("12$counterSTR")
                    transaction.set(code, arrayProduct)

                    arrayProduct = hashMapOf("name" to "Fabuloso 400ml", "amount" to 12, "price" to 1400.0f)
                    code = catAseo.document("11$counterSTR")
                    transaction.set(code, arrayProduct)

                    arrayProduct = hashMapOf("name" to "Arroz Libra", "amount" to 25, "price" to 2000.0f)
                    code = catComida.document("21$counterSTR")
                    transaction.set(code, arrayProduct)

                    arrayProduct = hashMapOf("name" to "Leche Colanta", "amount" to 8, "price" to 3600.0f)
                    code = catComida.document("22$counterSTR")
                    transaction.set(code, arrayProduct)

                    arrayProduct = hashMapOf("name" to "Lecherita Tubito", "amount" to 26, "price" to 800.0f)
                    code = catComida.document("23$counterSTR")
                    transaction.set(code, arrayProduct)

                    arrayProduct = hashMapOf("name" to "Tomate", "amount" to 2, "price" to 2200.0f)
                    code = catRevuelto.document("31$counterSTR")
                    transaction.set(code, arrayProduct)

                    arrayProduct = hashMapOf("name" to "Papa", "amount" to 10, "price" to 1700.0f)
                    code = catRevuelto.document("32$counterSTR")
                    transaction.set(code, arrayProduct)

                    arrayProduct = hashMapOf("name" to "Limon", "amount" to 3, "price" to 2500.0f)
                    code = catRevuelto.document("33$counterSTR")
                    transaction.set(code, arrayProduct)

                    counter += 1
                }
            }.addOnCompleteListener{
                Log.i("Creation transaction", "Success")
            }.addOnCanceledListener {
                Log.e("Creation transaction", "Error")
            }
        }
        return false
    }

    /**
     * Check if the user exists.
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
        val users = this.users.keys
        users.remove("")
        return users
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
            val categories = this.users[user]?.products?.keys
            categories?.remove("")
            categories
        } else null
    }

    /**
     * Rename category
     * @return 1 if the category was renamed. 2 if the original and new name are the same.
     * 0 if the new name is already assigned. 3 if the user does not exist
     */
    fun renameCategory(user: String, currentCategoryName: String, newCategoryName: String):Int{
        return if(containsUser(user)){
            if(currentCategoryName == newCategoryName){
                2
            } else if(containsCategory(user, newCategoryName) == 1){
                0
            }else{
                addCategory(user, newCategoryName)
                users[user]?.products?.set(newCategoryName, users[user]?.products?.get(currentCategoryName)!!)
                deleteCategory(user, currentCategoryName)
                1
            }
        }else{
            3
        }
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
            val products = this.users[user]?.products?.get(category)
            products?.remove("")
            return products
        }
        return null
    }
}