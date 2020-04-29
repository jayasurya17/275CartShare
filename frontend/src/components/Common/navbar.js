import React, { Component } from 'react';
import '../../css/navbar.css';

class Home extends Component {

    render() {

        if (this.props.isAdmin) {
            return (
                <div className="row text-center border-bottom pb-3 stickyNavBar bg-light">
                    <div class="col-md-2 border-left border-right"><a href="/admin/browse" class="text-secondary text-decoration-none"><p>View my stores</p></a></div>
                    <div class="col-md-2 border-left border-right"><a href="/admin/store/add" class="text-secondary text-decoration-none"><p>Add Stores</p></a></div>
                    <div class="col-md-2 border-left border-right"><a href="/admin/product/add" class="text-secondary text-decoration-none"><p>Add products</p></a></div>
                    <div class="col-md-2 border-left border-right"><a href="" class="text-secondary text-decoration-none"><p>View orders</p></a></div>
                    <div class="col-md-2 border-left border-right"><a href="" class="text-secondary text-decoration-none"><p>Something else</p></a></div>
                    <div class="col-md-2 border-left border-right"><a href="/pooler/update/account" class="text-secondary text-decoration-none"><p>Update account</p></a></div>
                </div>
            )
        }

        return (
            <div className="row text-center border-bottom pb-3 stickyNavBar bg-light">
                {/* <div class="col-md-2 border-left border-right"><a href="/pooler/browse" class="text-secondary text-decoration-none"><p>Browse stores</p></a></div> */}
                <div class="col-md-2 border-left border-right"><a href="/pooler/add/cart" class="text-secondary text-decoration-none"><p>Add to cart</p></a></div>
                <div class="col-md-2 border-left border-right"><a href="/pooler/view/pool" class="text-secondary text-decoration-none"><p>View Pool details</p></a></div>
                <div class="col-md-2 border-left border-right"><a href="/pooler/pickup" class="text-secondary text-decoration-none"><p>Orders to pickup</p></a></div>
                <div class="col-md-2 border-left border-right"><a href="/pooler/deliver" class="text-secondary text-decoration-none"><p>Orders to deliver</p></a></div>
                <div class="col-md-2 border-left border-right"><a href="/pooler/past/orders" class="text-secondary text-decoration-none"><p>My past orders</p></a></div>
                <div class="col-md-2 border-left border-right"><a href="/pooler/update/account" class="text-secondary text-decoration-none"><p>Update account</p></a></div>
            </div>
        )
    }
}
//export Home Component
export default Home;