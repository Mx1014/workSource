//
// EvhUpgradeInfoResponse.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhVersionDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpgradeInfoResponse
//
@interface EvhUpgradeInfoResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, strong) EvhVersionDTO* targetVersion;

@property(nonatomic, copy) NSNumber* forceFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

