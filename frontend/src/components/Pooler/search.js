import React, { Component } from 'react';
import Header from '../Common/header';
import Navigation from '../Common/navbar';
import ItemCard from '../Common/itemCard';

class Home extends Component {

    render() {

        return (
            <div>
                <Header />
                <Navigation />
                <div className="pl-5 pr-5">
                    
                    <div className="row mt-2">
                        <div className="col-md-6">
                            <ItemCard showQuantity={true}/>
                        </div>
                        <div className="col-md-6">
                            <ItemCard />
                        </div>
                    </div>
                    
                    <div className="row mt-2">
                        <div className="col-md-6">
                            <ItemCard />
                        </div>
                        <div className="col-md-6">
                            <ItemCard />
                        </div>
                    </div>
                    
                    <div className="row mt-2">
                        <div className="col-md-6">
                            <ItemCard />
                        </div>
                        <div className="col-md-6">
                            <ItemCard />
                        </div>
                    </div>
                    
                    <div className="row mt-2">
                        <div className="col-md-6">
                            <ItemCard />
                        </div>
                        <div className="col-md-6">
                            <ItemCard />
                        </div>
                    </div>
                    
                    <div className="row mt-2">
                        <div className="col-md-6">
                            <ItemCard />
                        </div>
                        <div className="col-md-6">
                            <ItemCard />
                        </div>
                    </div>
                    
                    <div className="row mt-2">
                        <div className="col-md-6">
                            <ItemCard />
                        </div>
                        <div className="col-md-6">
                            <ItemCard />
                        </div>
                    </div>

                </div>
            </div>
        )
    }
}
//export Home Component
export default Home;