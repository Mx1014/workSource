//
// EvhUserAccountDTO.h
// generated at 2016-04-08 20:09:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserAccountDTO
//
@interface EvhUserAccountDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* accountId;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* occupyFlag;

@property(nonatomic, copy) NSNumber* confId;

@property(nonatomic, copy) NSNumber* purchaseAuthority;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

