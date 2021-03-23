import axios from 'axios';

export default {
    getInvites() {
        return axios.get("/invitation")
    },
    getInvitee(uniqueId) {
        return axios.get(`/invitation/invitee/${uniqueId}`);
    },
    sendInvite(invitation) {
        return axios.post(`/invitation`, invitation)
    },
    getInviteById(id) {
        return axios.get(`/invitation/${id}`);
    },
    getInviteByUserId(id) {
        return axios.get(`/invitation/user/${id}`);
    },
    updateInvitee(invitee) {
        return axios.put('/invitation/invitee', invitee);
    },
    voteThumbsUp(inviteeVotes) {
        return axios.put('/invitation/thumbsup', inviteeVotes);
    },
    voteThumbsDown(inviteeVotes) {
        return axios.put('/invitation/thumbsdown', inviteeVotes);
    },
    undoVote(inviteeVotes) {
        return axios.put('/invitation/removevote', inviteeVotes);
    },
    undoAllVotes(inviteeVotes) {
        return axios.put('/invitation/removeallvotes', inviteeVotes);
    }



}