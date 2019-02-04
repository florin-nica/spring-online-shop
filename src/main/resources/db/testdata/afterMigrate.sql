MERGE INTO CUSTOMER (id, first_name, last_name, username)
  VALUES (1, 'Rebekah', 'Barry', 'Vivamus.rhoncus.Donec@convallis.net'),
         (2, 'Chloe', 'Hawkins', 'quam.Pellentesque@liberoet.org'),
         (3, 'Lyle', 'Ross', 'amet.ornare.lectus@pede.edu'),
         (4, 'Orson', 'Hicks', 'mollis.vitae.posuere@nonmassanon.ca'),
         (5, 'Adam', 'Grimes', 'amet.diam.eu@ullamcorpereueuismod.co.uk'),
         (6, 'Yasir', 'Reese', 'Sed.molestie.Sed@Aenean.org'),
         (7, 'Jael', 'Pierce', 'Pellentesque@scelerisque.org'),
         (8, 'Abigail', 'Harrell', 'vehicula.et@Donectempor.org'),
         (9, 'Upton', 'Bradley', 'iaculis.aliquet.diam@Suspendissenonleo.com'),
         (10, 'Shana', 'Harvey', 'varius.ultrices@et.edu');

MERGE INTO LOCATION (id, city, country, county, name, street)
  VALUES (1, 'Husum', 'Belarus', 'Saarland', 'Et Rutrum Non LLC', '769-9215 Pharetra Avenue'),
         (2, 'Vienna', 'Oman', 'Vienna', 'Nullam LLP', 'Ap #166-2784 Cubilia Av.'),
         (3, 'Bauchi', 'China', 'BA', 'Dui Cum Corporation', '318-4194 Egestas Avenue'),
         (4, 'Cork', 'Niue', 'Munster', 'Lorem LLC', '9220 Odio Rd.'),
         (5, 'Kurnool', 'Qatar', 'Andhra Pradesh', 'Auctor Limited', 'Ap #959-264 Risus. Avenue'),
         (6, 'Boo', 'Colombia', 'AB', 'Eget Dictum Placerat Consulting', '435-8479 Ultricies Road'),
         (7, 'Kano', 'Cape Verde', 'KN', 'Libero LLP', '738-6131 Justo. St.'),
         (8, 'Lo Prado', 'Mali', 'Metropolitana de Santiago', 'Diam Lorem Auctor Consulting',
          'P.O. Box 580, 9114 Erat Street'),
         (9, 'Bareilly', 'Jamaica', 'Uttar Pradesh', 'Gravida Aliquam Tincidunt LLC',
          'P.O. Box 169, 4801 Augue Avenue'),
         (10, 'Fairbanks', 'Saint Kitts and Nevis', 'AK', 'Aliquet Corp.', '231-3828 Risus. Ave');

MERGE INTO PRODUCT_CATEGORY (id, description, name)
  VALUES (1, 'porttitor eros nec tellus. Nunc', 'Notebook'),
         (2, 'magna a neque. Nullam ut', 'Headset'),
         (3, 'tincidunt, nunc ac mattis ornare,', 'Bike');

MERGE INTO SUPPLIER (id, name)
  VALUES (1, 'Dell'),
         (2, 'HP'),
         (3, 'Asus'),
         (4, 'Pegas'),
         (5, 'Bose');

MERGE INTO PRODUCT (id, description, name, price, weight, category_id, supplier_id)
  VALUES (1, 'auctor velit. Aliquam nisl. Nulla eu neque pellentesque massa lobortis', 'Dell Latitude E5570', 649.22, 4,
          1, 1),
         (2, 'orci, consectetuer euismod est arcu ac orci. Ut semper pretium', 'Asus Zenbook', 916.11, 2, 1, 3),
         (3, 'enim. Etiam imperdiet dictum magna. Ut tincidunt orci quis lectus.', 'HP Omen', 782.00, 3, 1, 2),
         (4, 'at lacus. Quisque purus sapien, gravida non, sollicitudin a, malesuada', 'Pegas', 644.99, 14, 3, 4),
         (5, 'Morbi non sapien molestie orci tincidunt adipiscing. Mauris molestie pharetra', 'Bose QC35', 403.32, 1, 2,
          5);


