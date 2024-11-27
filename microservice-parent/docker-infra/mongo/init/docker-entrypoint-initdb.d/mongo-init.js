print('START');

db = db.getSiblingDB('product-service');

// -- creating product-service DB
db.createUser(
    {
        user: 'admin',
        pwd: 'password',
        roles: [{role: 'readWrite', db: 'product-service'}]
    }
);

// -- creating admin user with readWrite permissions
// -- creating empty user collection in product-service DB
db.createCollection('user');

print('END');


// ---- Database initialization 'product-service'
// ---- Collection creation 'user' in the 'product-service' DB