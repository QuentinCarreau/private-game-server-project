import api from './api';

export const serverService = {

  /** GET /api/game-server → liste tous les serveurs */
  async getAllServers() {
    const response = await api.get('/game-server');
    return response.data;
  },

  /** GET /api/game-template → liste tous les templates de jeux */
  async getAllGamesTemplate() {
    const response = await api.get('/game-template');
    return response.data;
  },

  /** POST /api/game-server/{id}/launch → démarrer un serveur */
  async launchServer(serverId, gameId, maxPlayers) {
    const response = await api.post(`/game-server/${serverId}/launch`, {
      gameId,
      maxPlayers
    });
    return response.data;
  },

  /** POST /api/game-server/{id}/stop → arrêter un serveur */
  async stopServer(serverId) {
    const response = await api.post(`/game-server/${serverId}/stop`);
    return response.data;
  },

  /** POST /api/game-server/{id}/restart → redémarrer un serveur */
  async restartServer(serverId) {
    const response = await api.post(`/game-server/${serverId}/restart`);
    return response.data;
  }
};
