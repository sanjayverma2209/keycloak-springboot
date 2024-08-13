# keycloak-springboot

This is an implentation of keycloak integration with spring boot . Post installation we created two users each with different roles and then authorized them.
We have written a custom jwtAuthenticationConverter as the claim structure of the keycloak is different.

To run this 
1. Install Keycloak
2. Create a realm and then create a client_id.
3. Create users for the client_id and then assign roles to them
4. Test the login / logout APIs.
5.Run the above project and test the users against the endpoints.
