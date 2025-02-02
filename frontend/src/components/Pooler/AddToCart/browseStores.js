import React, { Component } from 'react';
import StoreCard from '../../Common/storeCard';
import axios from 'axios';


class BrowseStores extends Component {

    constructor() {
        super()
        this.state = {
            allStores: [],
            isFetched: false
        }
    }

    componentDidMount() {
        axios.get(`/store/all`)
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
                    <h2 className="font-weight-light text-center mt-5">Oops! Looks like there are no stores at the moment</h2>
                )
            }
        } else {
            let tempContainer = []
            for (var index in this.state.allStores) {
                tempContainer.push(
                    <div className="col-md-6">
                        <StoreCard storeObj={this.state.allStores[index]} setAsShopping={true} updateStore={this.props.updateStore} refresh={this.props.refresh} />
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
            <div className="pl-5 pr-5">

                {allStores}

            </div>
        )
    }
}
//export BrowseStores Component
export default BrowseStores;