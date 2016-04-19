//
// EvhUserAccountDTO.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
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

