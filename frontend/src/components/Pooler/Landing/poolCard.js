import React, { Component } from 'react';

class PoolCard extends Component {

    constructor(props) {
        super(props);
        this.state = {
            referenceName: "",
            knowsLeader: false,
            tempName: "",
            poolName: this.props.poolName ? this.props.poolName : "",
            description: this.props.description ? this.props.description : "",
            leader: this.props.leader ? this.props.leader : "",
            zip: this.props.zip ? this.props.zip : ""
        }
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
                                <button type="button" className="btn btn-primary">Send request</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default PoolCard;