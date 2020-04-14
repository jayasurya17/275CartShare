import React, { Component } from 'react';
import './navbar.css';

class Home extends Component {

    render() {

        return (
            <div className="row text-center border-bottom pb-3 stickyNavBar bg-light">
                <div class="col-md-2 border-left border-right"><a href="/pooler/browse" class="text-secondary text-decoration-none"><p>Browse stores</p></a></div>
                <div class="col-md-2 border-left border-right"><a href="/pooler/search" class="text-secondary text-decoration-none"><p>Search</p></a></div>
                <div class="col-md-2 border-left border-right"><a href="/pooler/update/account" class="text-secondary text-decoration-none"><p>Update account</p></a></div>
                <div class="col-md-2 border-left border-right"><a href="" class="text-secondary text-decoration-none"><p>Something else</p></a></div>
                <div class="col-md-2 border-left border-right"><a href="" class="text-secondary text-decoration-none"><p>Something else</p></a></div>
                <div class="col-md-2 border-left border-right"><a href="" class="text-secondary text-decoration-none"><p>Something else</p></a></div>
            </div>
        )
    }
}
//export Home Component
export default Home;