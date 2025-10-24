package Service;



    public class Provider {
        private String providerID;
        private String name;
        private String contact;
        private String email;
        private String address;

        public Provider(String providerID, String name, String contact, String email, String address) {
            this.providerID = providerID;
            this.name = name;
            this.contact = contact;
            this.email = email;
            this.address = address;
        }

        public String getProviderID() { return providerID; }
        public String getName() { return name; }
        public String getContact() { return contact; }
        public String getEmail() { return email; }
        public String getAddress() { return address; }

        @Override
        public String toString() {
            return "Provider ID: " + providerID +
                    "\nName: " + name +
                    "\nContact: " + contact +
                    "\nEmail: " + email +
                    "\nAddress: " + address;
        }
    }


