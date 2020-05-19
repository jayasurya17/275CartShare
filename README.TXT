Member 1
Farha Kauser | 014494974 | farha.kauser@sjsu.edu

Member 2
Jayasurya Pinaki | 014491854 | jayasurya.pinaki@sjsu.edu

Member 3
Maaz Sirkhot | 014279616 | maaz.sirkhot@sjsu.edu

Member 4
Shivan Desai | 010279646 | shivan.desai@sjsu.edu

URL to access the app
ec2-54-215-137-144.us-west-1.compute.amazonaws.com:3000


Instructions for TA
1) Image must be uploaded for creating or updating a product which is of size 512 KB or lower. The image will be stored in S3 and URL given by S3 will be used to display iamges.
2) Email service takes about 10 seconds per email. We are sorry about that.
3) For a pooler to browse/search all stores and products click on "CartShare" on the top left corner of the screen


Assumptions
1) If no image is given while creating a product, a placeholder image will be given
2) If no image is given while updating a product, old image will remain
3) An order is assumed to be fulfilled when a pooler picks up the order
4) When a pooler decides to use "let a fellow pooler pick up and deliver within 2 days" option. The status will be changed to cancelled even if the order has been picked up from store by a fellow pooler and has not yet been delivered.
5) When an order is marked as not delivered, contribution credit will be taken away from pooler who delivered and contribution credit will be added to the pooler who did not recieve the order


Build Instructions