import React, { Component } from 'react';
import axios from "axios";

class PoolCard extends Component {

    constructor(props) {
        super(props);
        this.state = {
            referenceName: "",
            knowsLeader: false,
            tempName: "",
            // poolName: this.props.poolName ? this.props.poolName : "",
            // description: this.props.description ? this.props.description : "",
            // leader: this.props.leader.screenName ? this.props.leader.screenName : "",
            // zip: this.props.zip ? this.props.zip : "",
            // leaderDetails: this.props.leader,
            referenceId: "",
            // poolId: this.props.poolId,
        }
        console.log(this.state);
        this.sendRequest = this.sendRequest.bind(this);
        this.knowsLeader = this.knowsLeader.bind(this);
        this.nameChangeHandler = this.nameChangeHandler.bind(this);
    }

    componentWillMount = () => {
        this.setState({
            poolName: this.props.poolName ? this.props.poolName : "",
            description: this.props.description ? this.props.description : "",
            // leader: this.props.leader.screenName ? this.props.leader.screenName : "",
            zip: this.props.zip ? this.props.zip : "",
            leaderDetails: this.props.leader,
            poolId: this.props.poolId
        })
    }

    knowsLeader = () => {
        if (this.state.knowsLeader) {
            this.setState({
                referenceName: this.state.tempName,
                knowsLeader: false,
            })
        } else {
            this.setState({
                referenceName: "",
                knowsLeader: true
            })
        }
    }

    nameChangeHandler = (e) => {
        if (this.state.knowsLeader === false) {
            this.setState({
                referenceName: e.target.value,
                tempName: e.target.value
            })
        }
    }

    sendRequest = async () => {
        this.setState({
            errorMsg: "",
            successMsg: ""
        })
        if (this.state.referenceName === "" && this.state.knowsLeader === false) {
            alert("Please provide required details");
        } else {
            console.log(this.state);
            if (this.state.knowsLeader === false) {
                await axios.get("/user/getUserByScreenName/" + this.state.referenceName)
                    .then(response => {
                        if (response.status === 200) {
                            console.log(response.data);
                            this.setState({
                                referenceId: response.data.id,
                                screenNameReference: true
                            })
                        }
                    })
                    .catch(() => {
                        this.setState({
                            errorMsg: "Request failed",
                            successMsg: "",
                            screenNameReference: false,
                            referenceId: ""
                        })
                    })
            }

            if (this.state.referenceId === "" && this.state.knowsLeader && this.state.screenNameReference === false) {
                alert("Please provide valid screen name");
            } else {
                let reference;
                if (this.state.screenNameReference) {
                    reference = this.state.referenceId
                } else {
                    reference = this.state.leaderDetails.id;
                }
                await axios.post("/poolMembers/joinPool", null, {
                    params: {
                        poolId: this.state.poolId,
                        userId: localStorage.getItem("275UserId"),
                        referenceId: reference
                    }
                })
                    .then(response => {
                        this.setState({
                            joinRequest: true,
                            successMsg: "You have successfully requested to join the pool",
                            errorMsg: "",
                            knowsLeader: false,
                        })
                    })
                    .catch(error => {
                        console.log(error.response.data);
                        this.setState({
                            joinRequest: false,
                            errorMsg: "Request failed",
                            successMsg: ""
                        })
                    })
            }
        }
    }

    render() {
        return (
            <div className="border rounded m-2 p-3 bg-white row">
                <div className="col-md-9">
                    <h2>Name: {this.state.poolName}</h2>
                    <h4 className="font-weight-light">{this.state.description}</h4>
                    <h4>Pool ID: <span className="font-weight-light">{this.props.alphaNumericId}</span> </h4>
                    <h4>{this.props.neighborhoodName} - <span className="font-weight-light">{this.state.zip}</span></h4>
                </div>
                <div className="col-md-3">
                    <button className="btn btn-warning" data-toggle="modal" data-target={"#ModalCenter" + this.state.poolId}>Join this pool</button>
                </div>

                {/* <!-- Modal --> */}
                <div className="modal fade" id={"ModalCenter" + this.state.poolId} tabIndex="-1" role="dialog" aria-labelledby="modalCenterTitle" aria-hidden="true">
                    <div className="modal-dialog modal-dialog-centered" role="document">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title" id="modalCenterTitle">Request to join poolname</h5>
                                <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div className="modal-body">
                                {
                                    this.state.errorMsg ? <p className="text-danger text-center">{this.state.errorMsg}</p> : null
                                }
                                {
                                    this.state.successMsg ? <p className="text-success text-center">{this.state.successMsg}</p> : null
                                }
                                <div className="form-group">
                                    <label>Referred by</label>
                                    <input type="text" className="form-control" placeholder="Screen name" value={this.state.referenceName} onChange={this.nameChangeHandler} />
                                </div>
                                <p>(or)</p>
                                <input type="checkbox" onClick={this.knowsLeader} />
                                <label> I know the pool leader</label>
                            </div>
                            <div className="modal-footer">
                                <button type="button" className="btn btn-secondary" data-dismiss="modal">Close</button>
                                <button type="button" className="btn btn-primary" onClick={this.sendRequest}>Send request</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default PoolCard;