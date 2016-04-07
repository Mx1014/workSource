//
// EvhFavoriteListFavoritesRestResponse.h
// generated at 2016-04-07 17:33:50 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFavoriteListFavoritesRestResponse
//
@interface EvhFavoriteListFavoritesRestResponse : EvhRestResponseBase

// array of EvhFavoriteDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
