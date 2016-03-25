//
// EvhFavoriteListFavoritesRestResponse.h
// generated at 2016-03-25 15:57:24 
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
