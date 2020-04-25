import React, { Component } from 'react';
import Header from '../../Common/header';
import Navigation from '../../Common/navbar';
import CartDetails from './cartDetails';
import UserContribution from './UserContribution';

class ViewCart extends Component {

    constructor() {
        super()
        this.state = {
            confirmOrder: false
        }
    }

    confirmOrder = () => {
        this.setState({
            confirmOrder: true
        })
    }

    render() {

        let action = []
        if (this.state.confirmOrder) {
            action = <UserContribution />
        } else {
            action = <CartDetails confirmOrder={this.confirmOrder} />
        }

        return (
            <div>
                <Header />
                <Navigation />
                
                { action }
                
            </div>
        )
    }
}
//export ViewCart Component
export default ViewCart;