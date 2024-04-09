/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cvbuilder.model;

/**
 *
 * @author mxgar
 */
public class User
{
    
        private String title;
        private String name;
        private String email;
        private String references;

        public User(String title, String name, String email, String references) {
            this.title = title;
            this.name = name;
            this.email = email;
            this.references = references;
        }

        public String getTitle() {
            return title;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
        
           public String getReferences() {
        return references;
    }
        
        
    @Override
    public String toString()
    {
        return "User{" + "name=" + name + '}';
    }
    
    
    public String getFormattedText() {
        return String.format("{\nuserProfileID:%s\ntitle:%s\nname:%s\nemail:%s\n}\n",
                title, name, email);
    }

}


