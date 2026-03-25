import api from './api';

export const serverService = {
  async getAllServers() {
    const response = await api.get('/servers');
    return response.data;
  },
  async launchServer(serverId, gameId, maxPlayers) {
    const response = await api.post(`/servers/${serverId}/launch`, {
      gameId,
      maxPlayers
    });
    return response.data;
  }
};
