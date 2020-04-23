import React, { Component } from 'react';
import Header from '../../Common/header';
import Navigation from '../../Common/navbar';
import StoreCard from '../../Common/storeCard';
import AddStoreComponent from './storeInfoComponent';
import axios from 'axios';
import constants from '../../../utils/constants'

class BrowseStores extends Component {

    constructor() {
        super()
        this.state = {
            allStores: []
        }
    }

    componentDidMount() {
        axios.get(`${constants.BACKEND_SERVER.URL}/store/all?adminId=${localStorage.getItem('275UserId')}`)
        .then((response) => {
            this.setState({
                allStores: response.data
            })
        })
    }

    render() {

        let allStores = []
        if (this.state.allStores.length === 0) {
            allStores.push(
                <h2 className="font-weight-light text-center mt-5">Oops! Looks like you do not have any stores at the moment</h2>
            )
            allStores.push(
                <div className="mb-5">
                    <AddStoreComponent />
                </div>
            )
        } else {
            let tempContainer = []
            for (var index in this.state.allStores) {
                tempContainer.push(
                    <div className="col-md-6">
                        <StoreCard isAdmin={true} storeObj={this.state.allStores[index]} />
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
                <Header isAdmin={true} />
                <Navigation isAdmin={true} />
                <div className="pl-5 pr-5">

                    {allStores}

                </div>
            </div>
        )
    }
}
//export BrowseStores Component
export default BrowseStores;