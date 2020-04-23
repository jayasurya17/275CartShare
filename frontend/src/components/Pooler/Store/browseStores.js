import React, { Component } from 'react';
import Header from '../../Common/header';
import Navigation from '../../Common/navbar';
import StoreCard from '../../Common/storeCard';
import axios from 'axios';
import constants from '../../../utils/constants';

class BrowseStores extends Component {

    constructor() {
        super()
        this.state = {
            allStores: [],
            isFetched: false
        }
    }

    componentDidMount() {
        axios.get(`${constants.BACKEND_SERVER.URL}/store/all`)
            .then((response) => {
                this.setState({
                    allStores: response.data,
                    isFetched: true
                })
            })
            .catch(() => {
                this.setState({
                    isFetched: true
                })
            })
    }

    render() {

        let allStores = []
        if (this.state.allStores.length === 0) {
            if (this.state.isFetched === true) {
                allStores.push(
                    <h2 className="font-weight-light text-center mt-5">Oops! Looks like there are not any stores at the moment</h2>
                )
            }
        } else {
            let tempContainer = []
            for (var index in this.state.allStores) {
                tempContainer.push(
                    <div className="col-md-6">
                        <StoreCard storeObj={this.state.allStores[index]} />
                    </div>
                )
                if ((index + 1) % 2 === 0) {
                    allStores.push(
                        <div className="row mt-2">
                            {tempContainer}
                        </div>
                    )
                    tempContainer = []
                }
            }
            allStores.push(
                <div className="row mt-2">
                    {tempContainer}
                </div>
            )
        }

        return (
            <div>
                <Header />
                <Navigation />
                <div className="pl-5 pr-5">

                    {allStores}

                </div>
            </div>
        )
    }
}
//export BrowseStores Component
export default BrowseStores;