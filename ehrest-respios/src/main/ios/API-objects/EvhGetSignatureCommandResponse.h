//
// EvhGetSignatureCommandResponse.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:50 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
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

