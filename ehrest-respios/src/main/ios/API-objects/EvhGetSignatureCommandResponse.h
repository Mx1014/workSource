//
// EvhGetSignatureCommandResponse.h
// generated at 2016-04-08 20:09:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetSignatureCommandResponse
//
@interface EvhGetSignatureCommandResponse
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

