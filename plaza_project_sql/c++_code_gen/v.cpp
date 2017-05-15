//#include <stdio.h>    //unused for printf
//#include <ctime>      //used for time

#include <iostream>

#include <vector>       //used for vectors

#include <numeric>      //used for iota

#include <algorithm>    //used for shuffling
#include <random>       //used for random distributions
#include <cstdlib>      //used for rand() aka simple numbers

#include <thread>       //used for sleeping
#include <chrono>        //used for seconds

#include "plaza_id.cpp"

/*  TODO
        - Store names + Generate Stores + Item Names

        - test cases
        - normalize naming
* 0) Organize actual values (owner, fake address, items range, etc)/
  * 1) Generate Random Elements (first do one)
  * 2) Put it into a struct (access through some key)
  * 3) Randomly link them through store sells
  *
  * Optomizations:
  *  0) Link to our data
  *  1) Print to different files 
*/

//====================================================================
/* Setup */

typedef unsigned short us;

//Both
size_t number_of_items_generated = 5;
size_t number_of_stores = 5;

size_t number_of_items_associated = 2;

//Items
size_t item_offset = 8000;
size_t generated_store_name_length = 10;

size_t generated_item_name_length = 10;

size_t item_type_offset = 5555;
size_t number_of_item_types = 60;

//Stores

size_t street_number_range = 9000;
size_t street_name_length = 10;

size_t year_start = 1923;
size_t year_range = 94;


size_t store_id_offset = 80;
size_t store_type_id_offset = 36;

size_t number_of_store_type_ids = store_type_name.size();

size_t owner_id_offset = 500; //sql has 0-800
size_t number_of_owners = 72;

size_t plaza_id_offset = 4444;
size_t number_of_plazas = 300;



/* Generator symbols */ //NOTE
std::string lowercase_alphabet  = "abcdefghijklmnopqrstuvwxyz";
std::string uppercase_alphabet   = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
std::string numbers             = "0123456789";
std::string symbols             = "~!@#$%^&*()_+-=[]\\;',./{}|:\"<>?";
std::vector<std::string> lowers_uppers_numbers = {lowercase_alphabet, uppercase_alphabet, numbers};

/* Vectors of Sample Labels */ //NOTE
// Owners
std::vector<std::string> list_of_10_first_names = {"Sophia", "Emma", "Jackson", "Aiden", "Olivia", "Lucas", "Ava", "Liam"};
//Storefronts
std::vector<std::string> storefront_names       = {"Generic Store", "Generic Warehouse", "Generic Seller"};
//Items
std::vector<std::string> item_names             = {"Generic Boxy Item", "An Item A", "QTD Circular Thing"};
//Streets
std::vector<std::string> street_names           = {"Boulevard", "Aveneue", "Way", "Street"};

/* Structs */

struct Store{

    std::string name = "72_";
    std::string store_id = "72_noidyet";
    std::string phone_number = "72_72";
    std::vector<int> item_ids_sold = {72}; //TODO make consisnt with string not int


    std::string address     = "72 address";
    std::string year_opened = "19 7272";
    std::string type_id     = "72 type";
    std::string plaza_id    = "72 plaza";
    std::string owner_id    = "72 owner";


};

struct Item{

    std::string name    = "72_";
    std::string id      = "72";
    std::string price   = "72.27";
    std::string url     = "http://72.27";

    std::string type    = "72 item type";
};

//====================================================================
/* Randomization Functions */

size_t generate_bounded_number(size_t limit){

    return std::rand() % limit;
}


//NOTE Vectors
template <class T>
int generate_random_index_from_vector(std::vector<T> const &vector_T){

    return std::rand() % vector_T.size();
}
template <class T>
T* get_random_element_from_vector(std::vector<T> &vector_T){ //apparently cant put const here

    return &(vector_T[generate_random_index_from_vector(vector_T)]);
}

//Strings
int generate_random_index_from_string(std::string const &string_T){

    return std::rand() % string_T.size();
}

char get_random_char_from_string(std::string &string_T){
    return (char) string_T[generate_random_index_from_string(string_T)];
}

//Item Name
std::string generate_item_name(size_t length){


    std::string item_name = "";

    //Creates "Gernic Name" in front
    std::string *a = get_random_element_from_vector(item_names);
    item_name += *a + " ";

    //Randomly selects characters
    for (size_t i = 0; i < length ; ++i){
        std::string *b = get_random_element_from_vector(lowers_uppers_numbers);
        item_name += get_random_char_from_string(*b);
    }

    return item_name;
}

//Store Name
std::string generate_store_name(size_t length){


    std::string store_name = "";

    //Creates "Gernic Name" in front
    std::string *a = get_random_element_from_vector(storefront_names);
    store_name += *a + " ";

    //Randomly selects characters
    for (size_t i = 0; i < length ; ++i){
        std::string *b = get_random_element_from_vector(lowers_uppers_numbers);
        store_name += get_random_char_from_string(*b);
    }

    return store_name;
}


//Store Phone Number 1(DDD)-DDD-DDDD TODO Fix labeling
std::string generate_store_phone_number(){
    
    std::string phone_number_string = "1(";

    int temp = 72000 + rand() % 1000;
    phone_number_string += std::to_string(temp).substr(2,3) + ")-";
        temp = 72000 + rand() % 1000;
    phone_number_string += std::to_string(temp).substr(2,3) + "-";
        temp = 720000 + rand() % 10000;
    phone_number_string += std::to_string(temp).substr(2,4);
    
    return phone_number_string;
}

//Generate Street Address
std::string generate_street_address(size_t length){

    std::string street_address = std::to_string(rand() % street_number_range) + " ";

    //Randomly selects characters
    for (size_t i = 0; i < length ; ++i){
        std::string *b = get_random_element_from_vector(lowers_uppers_numbers);
        street_address += get_random_char_from_string(*b);
    }

    //Creates "Gernic Name" in front
    std::string *a = get_random_element_from_vector(street_names);
    street_address += " " + *a;

    return street_address;
}

//Generate Year Opened
std::string generate_year_opened(size_t year_start, size_t year_range){

    return std::to_string((year_start + rand() % year_range));
}

//Generate Store ID
std::string generate_store_id(size_t store_id_offset, size_t number_of_stores){

    return std::to_string(store_id_offset + rand() % number_of_stores);
}

//Generate Store Type ID
std::string generate_type_id(size_t store_type_id_offset, size_t number_of_store_type_ids){

    return std::to_string(store_type_id_offset + rand() % number_of_store_type_ids);
}

//Generate Owner ID
std::string generate_owner_id(size_t owner_id_offset, size_t number_of_owners){

    return std::to_string(owner_id_offset + rand() % number_of_owners);
}

//Generate Plaza ID
std::string generate_plaza_id(size_t plaza_id_offset, size_t number_of_plazas){

    return std::to_string(plaza_id_offset + rand() % number_of_plazas);
}

//Generate Item Price
std::string generate_item_price(){

    std::string item_price = "$";

    item_price += std::to_string(rand() % 100) + ".";
    item_price += std::to_string(7200 + rand() % 100).substr(2,2);

    return item_price;
}

//Generate Random Item Type
std::string generate_item_type(size_t store_type_id_offset, size_t number_of_store_type_ids){//TODO bozo code

    return std::to_string(store_type_id_offset + rand() % number_of_store_type_ids);
}


//====================================================================
/* Data Filling */

void clear_and_fill_store_with__random_items(   Store               &s, 
                                                std::vector<int>    item_id_vector, 
                                                size_t              quantity
                                            ){ //TODO 1) naming normalize 2) throw exceptions

    if (quantity > item_id_vector.size()){
        std::cout << "Items associated number greater than generated items. Using maximum items as value instead";
        quantity = item_id_vector.size();
    }

    /* Shuffle the item_id_vector */
    auto engine = std::default_random_engine(rand());


    std::shuffle(std::begin(item_id_vector), std::end(item_id_vector), engine);

    /* Clear the store item vector */
    s.item_ids_sold.clear();

    /* Do this 100x */
    for (us i = 0; i < quantity; i++){

        ///* Get a random item id to add */
        //int rand_item_index = generate_random_index_from_vector(item_id_vector);

        //int item_id_to_add = item_id_vector.pop(rand_item_index);
        
        /* Push it to the vector */
        s.item_ids_sold.push_back(item_id_vector.back());
        /* Remove the last item from the 'item_id_vector' */
        item_id_vector.pop_back();
    }
}


std::vector<Item> *create_number_of_items(size_t quantity, size_t offset){

   std::vector<Item> *new_item_vector = new std::vector<Item>();


   for (size_t i = 0; i < quantity; ++i){

       Item new_item;
       new_item_vector->push_back(new_item);
       (*new_item_vector)[i].id = offset + i;
   } 

   return new_item_vector;

}

void insert_ids_into_item_vector(auto &item_instances, size_t item_offset, size_t number_of_items_generated){ //TODO Figure out # scheme

    if (item_instances.size() >= number_of_items_generated){

        auto a = std::begin(item_instances);
    
        while (a != std::end(item_instances)){
            a->id =  std::to_string(item_offset);
            item_offset++;
            a++;
        }
    }
    else{
        std::cout << "Too many items id; not large enough item vector";
    }
}

//Fill item type randomly for item instance
void create_types_for_items(auto &item_instances){ //TODO passing parameters consistency

        auto a = std::begin(item_instances);
        while (a != std::end(item_instances)){
            a->type = generate_item_type(store_type_id_offset, number_of_store_type_ids);//NOTE BOZO CODE
            a++;
        }
}


//Fill addresses randomly for store instance
void create_addresses_for_stores(auto &store_instances){ //TODO passing parameters consistency

        auto a = std::begin(store_instances);
        while (a != std::end(store_instances)){
            a->address = generate_street_address(street_name_length);
            a++;
        }
}

//Fill year opened randomly for store instance
void create_years_opened_for_stores(auto &store_instances){ //TODO passing parameters consistency

        auto a = std::begin(store_instances);
        while (a != std::end(store_instances)){
            a->year_opened = generate_year_opened(year_start, year_range);
            a++;
        }
}

//Fill id randomly for store instance
void create_store_ids_for_stores(auto &store_instances, size_t store_id_offset){ //TODO passing parameters consistency

        auto a = std::begin(store_instances);
        while (a != std::end(store_instances)){
            a->store_id = std::to_string(store_id_offset++);
            a++;
        }
}

//Fill type randomly for store instance
void create_type_ids_for_stores(auto &store_instances){ //TODO passing parameters consistency

        auto a = std::begin(store_instances);
        while (a != std::end(store_instances)){
            a->type_id = generate_type_id(store_type_id_offset, number_of_store_type_ids);
            a++;
        }
}

//Fill owner id randomly for store instance
void create_owner_ids_for_stores(auto &store_instances){ //TODO passing parameters consistency

        auto a = std::begin(store_instances);
        while (a != std::end(store_instances)){
            a->owner_id = generate_owner_id(owner_id_offset, number_of_owners);
            a++;
        }
}

//Fill plaza id randomly for store instance
void create_plaza_ids_for_stores(auto &store_instances){ //TODO passing parameters consistency

        long *c = get_random_element_from_vector(plaza_ids);

        auto a = std::begin(store_instances);
        while (a != std::end(store_instances)){
            a->plaza_id = std::to_string(*c);
            c = get_random_element_from_vector(plaza_ids);
            a++;
        }
}




//====================================================================

void kappa(){


    /* Fills vector 'item_id_vector' with 1000 values starting from 8000 -> 8999 */
    std::vector<int> item_id_vector(number_of_items_generated);
    std::iota(std::begin(item_id_vector), std::end(item_id_vector), item_offset);
    

    //std::srand(0); /* 'rand()' requires a seed using srand. I'll use '0' for our purposes */
    
    //for (us i =0; i < 100; i++){ TODO APPEND OWNER NAMES right
    //    std::string random_name = list_of_10_first_names[generate_random_index_from_vector(list_of_10_first_names)];
    //    random_name.append(" "); random_name.append(std::to_string(generate_number_0_to_100()));
    //
    //    std::cout << random_name << std::endl;
    //


    //std::srand(time(NULL));

    std::random_device rd; //NOTE
    std::srand(rd());

    //Create stores
    std::vector<Store>  store_instances(number_of_stores);
    //Create items
    std::vector<Item>   item_instances(number_of_items_generated);

    //Give names and things to sell
    auto e = std::begin(store_instances);
    while (e != std::end(store_instances)){

        e->name = generate_store_name(generated_store_name_length);
        e->phone_number = generate_store_phone_number();

        clear_and_fill_store_with__random_items(*e, item_id_vector, number_of_items_associated);

        ++e;
    }

    //Give names to items
    auto b = std::begin(item_instances);
    while (b != std::end(item_instances)){

        b->name = generate_item_name(generated_item_name_length);
        b->price = generate_item_price();

        ++b;
    }
    //Give items their "ids"
    insert_ids_into_item_vector(item_instances, item_offset, number_of_items_generated);


    for (auto e: store_instances[0].item_ids_sold){
        std::cout << e << std::endl;
    }


    //std::this_thread::sleep_for(std::chrono::seconds(5));
    //std::srand(time(NULL));

    //std::vector<Item> *item_vector_pointer = create_number_of_items(number_of_items_generated,item_offset);
    //std::cout << (*item_vector_pointer)[199].id << std::endl;

    //std::cout << generate_item_name(generated_item_name_length) << std::endl;

}


