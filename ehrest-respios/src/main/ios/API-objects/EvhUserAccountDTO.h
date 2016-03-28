//
// EvhUserAccountDTO.h
// generated at 2016-03-28 15:56:07 
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

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

