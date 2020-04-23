import React, { Component } from 'react';
import '../../css/header.css';

class Home extends Component {

    render() {

        let cartButton = [
            <div className="row">
                <div className="col-md-12 text-center">
                    <a href="/pooler/view/cart">
                        <button className="btn btn-outline-success w-50">View Cart</button>
                    </a>
                </div>
            </div>,
            <div className="row">
                <div className="col-md-12 text-center">
                    <p className="text-center">Not {localStorage.getItem('275NickName')}? <a href="/logout" className="text-decoration-none">Logout</a></p>
                </div>
            </div>
        ]
        if (this.props.isLanding || this.props.isAdmin) {
            cartButton = [
                <div className="row pt-3">
                    <div className="col-md-12 text-center">
                        <p className="text-center">Not {localStorage.getItem('275NickName')}? <a href="/logout" className="text-decoration-none">Logout</a></p>
                    </div>
                </div>
            ]
        }

        return (
            <div className="row pl-3 pt-1 pb-1 stickyHeader bg-light text-secondary">
                <div className="col-md-8">
                    <h1 class="display-4">CartShare</h1>
                </div>
                <div className="col-md-2 offset-md-2">
                    { cartButton }
                </div>
            </div>
        )
    }
}
//export Home Component
export default Home;