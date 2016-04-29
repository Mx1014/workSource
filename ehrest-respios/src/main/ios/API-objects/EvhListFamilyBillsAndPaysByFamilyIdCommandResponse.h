//
// EvhListFamilyBillsAndPaysByFamilyIdCommandResponse.h
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
#import "EvhPmBillsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListFamilyBillsAndPaysByFamilyIdCommandResponse
//
@interface EvhListFamilyBillsAndPaysByFamilyIdCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* billDate;

@property(nonatomic, copy) NSNumber* nextPageOffset;

@property(nonatomic, copy) NSString* orgTelephone;

@property(nonatomic, copy) NSString* orgName;

@property(nonatomic, copy) NSString* zlTelephone;

@property(nonatomic, copy) NSString* zlName;

@property(nonatomic, copy) NSNumber* orgIsExist;

// item type EvhPmBillsDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

