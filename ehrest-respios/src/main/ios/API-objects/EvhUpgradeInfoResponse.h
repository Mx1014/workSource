//
// EvhUpgradeInfoResponse.h
// generated at 2016-03-25 17:08:12 
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

