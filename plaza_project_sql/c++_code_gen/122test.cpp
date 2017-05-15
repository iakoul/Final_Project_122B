#include <iomanip>
#include <iostream>
#include "v.cpp"

void print_test(std::vector<Store> &stores, std::vector<Item> &items);


int  main(){

    /* Fills vector 'item_id_vector' with 1000 values starting from 8000 -> 8999 */
    std::vector<int> item_id_vector(number_of_items_generated);
    std::iota(std::begin(item_id_vector), std::end(item_id_vector), item_offset);

//    for (auto e: item_id_vector)
//        std::cout << e << std::endl;

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

    create_addresses_for_stores(store_instances);
    create_years_opened_for_stores(store_instances);
    create_store_ids_for_stores(store_instances, store_id_offset);
    create_type_ids_for_stores(store_instances);
    create_owner_ids_for_stores(store_instances);
    create_plaza_ids_for_stores(store_instances);



    //Give names to items
    auto b = std::begin(item_instances);
    while (b != std::end(item_instances)){

        b->name = generate_item_name(generated_item_name_length);
        b->price = generate_item_price();

        ++b;
    }
    //Give items their "ids"
    insert_ids_into_item_vector(item_instances, item_offset, number_of_items_generated);
    //Give items their "types"
    create_types_for_items(item_instances);

    //EXPORTING
    print_test(store_instances,item_instances);


}

void print_test(std::vector<Store> &stores, std::vector<Item> &items){ //TODO come up with print naming conventions
// PRINTING ===================================================================================

//    std::string storeID          = "62601033";
//    std::string storeName        = "Bobby's Super Burgers";
//    std::string address          = "132 Lemington Way";
//    std::string storePhoneNumber = "1-626-608-8954";
//    std::string yearOpened       = "2015";
//    std::string typeID           = "3434343";
//    std::string typeName         = "Restaurant";
//    std::string plazaID          = "Big Reef Plaza";
//    std::string ownerID          = "Bobby";

    std::string storeID          = "blank storeid 72";
    std::string storeName        = "blank storename 72";
    std::string address          = "blank address 72";
    std::string storePhoneNumber = "black phoneNumber 72";
    std::string yearOpened       = "blank year 72";
    std::string typeID           = "blank type id 72";
    std::string typeName         = "blank typeStore 72";
    std::string plazaID          = "blank plaza 72";
    std::string ownerID          = "blank owner 72";



    std::cout << "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" << std::endl;
    std::cout << "<input>" << std::endl;



    std::cout << "    <store-types>\n";
    for (size_t i = 0; i < number_of_store_type_ids; i++)
        std::cout << "        <store-type type-id=\"" << std::left << std::setw(7) << std::to_string(store_type_id_offset + i) + "\">" << std::left << std::setw(30) <<  store_type_name[i]  << "</store-type>\n";
    std::cout << "    </store-types>" << std::endl << std::endl;

    std::string itemPic         = "7272 http://393i3j34.33oods.urltxt/picthisthat.sandwich.vox.spam.png"; //TODO


//NOTE
    auto item_instance_i = std::begin(items);
    std::cout << "    <items>\n";                   
        while (item_instance_i != std::end(items)){
            std::cout << "         "        << "<item item-id=\""   << item_instance_i->id << "\">\n";         
            std::cout << "              "   << std::left << std::setw(25) << "<item-name>"  << std::left << std::setw(35)  <<  item_instance_i->name    << "</item-name>\n";
            std::cout << "              "   << std::left << std::setw(25) << "<item-type>"  << std::left << std::setw(35)  <<  item_instance_i->type    << "</item-type>\n";
            std::cout << "              "   << std::left << std::setw(25) << "<item-price>" << std::left << std::setw(35)  <<  item_instance_i->price   << "</item-price>\n";
            std::cout << "              "   << std::left << std::setw(25) << "<item-pic>"   << std::left << std::setw(80)  <<  itemPic                  << "</item-pic>\n";      //TODO
            std::cout << "         "        << "</item>\n";
            item_instance_i++;    
        }
    std::cout << "    </items>" << std::endl << std::endl; 


//NOTE
    std::cout << "    <stores>\n";
      auto store_instance_i = std::begin(stores);
        while (store_instance_i != std::end(stores)){

            std::cout << "         "        << "<store store-id=\""   << store_instance_i->store_id << "\">\n";                  
            std::cout << "              "   << std::left << std::setw(25) << "<store-name>"         << std::left << std::setw(30)  <<  store_instance_i->name               << "</store-name>\n";        
            std::cout << "              "   << std::left << std::setw(25) << "<address>"            << std::left << std::setw(30)  <<  store_instance_i->address            << "</address>\n";           
            std::cout << "              "   << std::left << std::setw(25) << "<store-phone-number>" << std::left << std::setw(30)  <<  store_instance_i->phone_number       << "</store-phone-number>\n";
            std::cout << "              "   << std::left << std::setw(25) << "<year-opened>"        << std::left << std::setw(30)  <<  store_instance_i->year_opened        << "</year-opened>\n";       
            std::cout << "              "   << std::left << std::setw(25) << "<type-id>"            << std::left << std::setw(30)  <<  store_instance_i->type_id            << "</type-id>\n";           
            std::cout << "              "   << std::left << std::setw(25) << "<plaza-id>"           << std::left << std::setw(30)  <<  store_instance_i->plaza_id           << "</plaza-id>\n";          
            std::cout << "              "   << std::left << std::setw(25) << "<owner-id>"           << std::left << std::setw(30)  <<  store_instance_i->owner_id           << "</owner-id>\n";          
            std::cout << "         "        << "</store>\n";
        
            store_instance_i++;
        }
      std::cout << "    </stores>" << std::endl << std::endl;


    
    std::cout << "    <store-sells>\n";
    //For each store
    store_instance_i = std::begin(stores);
    while (store_instance_i != std::end(stores)){
            //For all items in that store
            for (auto e : store_instance_i->item_ids_sold){
                std::cout << "        "     <<  "<store-sell>\n";
                std::cout << "              "   << std::left << std::setw(12) << "<store-id>" << std::left << std::setw(10)  <<  store_instance_i->owner_id     << "</store-id>\n";
                std::cout << "              "   << std::left << std::setw(12) << "<item-id>"  << std::left << std::setw(10)  <<  std::to_string(e)              << "</item-id>\n";          
                std::cout << "        "     <<  "</store-sell>\n";
            }
        store_instance_i++;
    }

    std::cout << "    </store-sells>" << std::endl << std::endl;




//Deprec
//
//  std::cout << "    <owners>\n";
//  std::cout << "         "        << "<owner owner-id=\""   << ownerID << "\"\n";                  
//  std::cout << "              "   << std::left << std::setw(12) << "<owner-name>"  << std::left << std::setw(20)  <<  ownerID     << "</owner-id>\n";          
//  std::cout << "              "   << std::left << std::setw(12) << "<phone-number" << std::left << std::setw(20)  <<  phoneNumber << "</phone-number>\n";          
//  std::cout << "        "     <<  "</owner>\n";
//  std::cout << "    </owners>" << std::endl;


    std::cout << "</input>" << std::endl;

}
