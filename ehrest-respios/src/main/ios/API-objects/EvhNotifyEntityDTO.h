//
// EvhNotifyEntityDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNotifyEntityDTO
//
@interface EvhNotifyEntityDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* msg_sn;

@property(nonatomic, copy) NSString* return_code;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

