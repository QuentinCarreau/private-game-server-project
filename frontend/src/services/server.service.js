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

  /** GET /api/game-server/{id} → obtenir un serveur par ID */
  async getGameServerById(serverId) {
    const response = await api.get(`/game-server/${serverId}`);
    return response.data;
  },

  /** POST /api/game-server → créer un serveur */
  async createGameServer(gameServer) {
    const response = await api.post('/game-server/create', gameServer);
    return response.data;
  },

  /** POST /api/game-server/{id}/launch → démarrer un serveur (pas de body) */
  async launchServer(serverId) {
    const response = await api.post(`/game-server/${serverId}/launch`);
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
  },

  /** PUT /api/game-server/{id} → mettre à jour un serveur */
  async updateGameServer(serverId, data) {
    const response = await api.put(`/game-server/${serverId}`, data);
    return response.data;
  },

  /** DELETE /api/game-server/{id} → supprimer un serveur */
  async deleteGameServer(serverId) {
    await api.delete(`/game-server/${serverId}`);
  }
};
