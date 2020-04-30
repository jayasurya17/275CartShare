import React, { Component } from 'react';
import axios from "axios";

class PoolCard extends Component {

    constructor(props) {
        super(props);
        this.state = {
            referenceName: "",
            knowsLeader: false,
            tempName: "",
            poolName: this.props.poolName ? this.props.poolName : "",
            description: this.props.description ? this.props.description : "",
            leader: this.props.leader.screenName ? this.props.leader.screenName : "",
            zip: this.props.zip ? this.props.zip : "",
            leaderDetails : this.props.leader,
            referenceId : "",
            poolId : this.props.poolId
        }
        this.sendRequest = this.sendRequest.bind(this);
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
        if(this.state.referenceName === "" && this.state.knowsLeader === false){
            alert("Please provide required details");
        } else {
            if(this.state.knowsLeader === false){
                await axios.get("/user/getUserByScreenName/" + this.state.referenceName)
                .then(response => {
                    if(response.status === 200){
                        console.log(response.data);
                        this.setState({
                            referenceId : response.data.id,
                            screenNameReference : true
                        })
                    }
                })
                .catch(error => {
                    console.log(error.response.data);
                    this.setState({
                        screenNameReference : false,
                        referenceId : ""
                    })
                })
            }

            if(this.state.referenceId === "" && this.state.knowsLeader && this.state.screenNameReference === false){
                alert("Please provide valid screen name");
            }
            else {
                let reference;
                if(this.state.screenNameReference){
                    reference = this.state.referenceId
                } else {
                    reference = this.state.leaderDetails.id;
                }
                await axios.post("/poolMembers/joinPool", null, {
                    params : {
                        poolId : this.state.poolId,
                        userId : localStorage.getItem("275UserId"),
                        referenceId : reference
                    }
                })
                .then(response => {
                    if(response.status === 200){
                        this.setState({
                            joinRequest : true,
                            successMsg : "Pool Joined Successfully",
                            errorMsg : ""
                        })
                    } else {
                        this.setState({
                            joinRequest : false,
                            errorMsg : "Pool Join Failed",
                            successMsg : ""
                        })
                    }
                })
                .catch(error => {
                    console.log(error.response.data);
                    this.setState({
                        joinRequest : false,
                        errorMsg : "Pool Join Failed",
                        successMsg : ""
                    })
                })
            }        
        }
    }

    render() {
        return (
            <div className="border rounded m-2 p-3 bg-white row">
                <div className="col-md-9">
                    <h2>{this.state.poolName}</h2>
                    <h4 className="font-weight-light">{this.state.description}</h4>
                    <h4>Pool leader: <span className="font-weight-light">{this.state.leader}</span> </h4>
                    <h4>San Jose <span className="font-weight-light">{this.state.zip}</span></h4>
                </div>
                <div className="col-md-3">
                    <button className="btn btn-warning" data-toggle="modal" data-target="#modalCenter">Join this pool</button>
                </div>

                {/* <!-- Modal --> */}
                <div className="modal fade" id="modalCenter" tabIndex="-1" role="dialog" aria-labelledby="modalCenterTitle" aria-hidden="true">
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
                                this.state.errorMsg ? <p className="text-danger">{this.state.errorMsg}</p> : null
                            }
                            {
                                this.state.successMsg ? <p className="text-success">{this.state.successMsg}</p> : null
                            }
                                <div className="form-group">
                                    <label>Referred by</label>
                                    <input type="text" className="form-control" value={this.state.referenceName} onChange={this.nameChangeHandler}/>
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