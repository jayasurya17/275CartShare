import React, { Component } from 'react';
import '../../../css/browsePool.css';
import PoolCard from './poolCard';

class Home extends Component {

    render() {

        return (
            <div>
                <input className="form-control" placeholder="Search by pool name or neighborhood name or zipcode" />
                <div className="scrollable">
                    <PoolCard />
                    <PoolCard />
                    <PoolCard />
                    <PoolCard />
                    <PoolCard />
                </div>
            </div>
        )
    }
}

//export Home Component
export default Home;