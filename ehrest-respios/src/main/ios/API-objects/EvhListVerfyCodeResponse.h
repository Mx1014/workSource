//
// EvhListVerfyCodeResponse.h
// generated at 2016-04-18 14:48:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhUserIdentifierDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListVerfyCodeResponse
//
@interface EvhListVerfyCodeResponse
    : NSObject<EvhJsonSerializable>


// item type EvhUserIdentifierDTO*
@property(nonatomic, strong) NSMutableArray* values;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

