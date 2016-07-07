//
// EvhUserGetSignatureCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserGetSignatureCommandResponse
//
@interface EvhUserGetSignatureCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* signature;

@property(nonatomic, copy) NSString* appKey;

@property(nonatomic, copy) NSNumber* timeStamp;

@property(nonatomic, copy) NSNumber* randomNum;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

