//
// EvhUserListTreasureRestResponse.h
// generated at 2016-04-29 18:56:04 
//
#import "RestResponseBase.h"
#import "EvhListTreasureResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserListTreasureRestResponse
//
@interface EvhUserListTreasureRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListTreasureResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
