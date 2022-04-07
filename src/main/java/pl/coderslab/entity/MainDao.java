package pl.coderslab.entity;

public class MainDao {

    public static void main(String[] args) {


        UserDao userDao= new UserDao();
        User kamil= new User();

          //opcja create
          kamil.setEmail("kamil19@interia.pl");
          kamil.setUserName("Karol");
          kamil.setPassword("pass");
          userDao.create(kamil);

          //opcja read
          User read= userDao.read(1);
          System.out.println(read);
  
          // opcja update
          User userUpdate=userDao.read(4);
          userUpdate.setEmail("kamil199@interia.pl");
          userUpdate.setUserName("Karol");
          userUpdate.setPassword("pass");
          userDao.update(userUpdate);

       //opcja usuniecia
         userDao.delete(6);

        //opcja findAll

        User[] usersArray= userDao.findAll();
        for(User k:usersArray){
            System.out.println(k);
        }

    }





}
