//
// EvhConfAccountOrderDTO.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfAccountOrderDTO
//
@interface EvhConfAccountOrderDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* billId;

@property(nonatomic, copy) NSNumber* amount;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* orderType;

@property(nonatomic, copy) NSString* appKey;

@property(nonatomic, copy) NSNumber* timestamp;

@property(nonatomic, copy) NSNumber* randomNum;

@property(nonatomic, copy) NSString* signature;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

