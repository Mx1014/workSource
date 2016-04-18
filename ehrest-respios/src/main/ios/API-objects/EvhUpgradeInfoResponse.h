//
// EvhUpgradeInfoResponse.h
// generated at 2016-04-18 14:48:51 
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

